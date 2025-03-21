package com.eigtheenthstreet.order_service.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eigtheenthstreet.order_service.application.dto.CompanyType;
import com.eigtheenthstreet.order_service.application.dto.CreateOrderResponse;
import com.eigtheenthstreet.order_service.application.dto.SelectOrderResponse;
import com.eigtheenthstreet.order_service.application.dto.UpdateOrderResponse;
import com.eigtheenthstreet.order_service.domain.model.Order;
import com.eigtheenthstreet.order_service.domain.model.OrderItem;
import com.eigtheenthstreet.order_service.domain.model.OrderStatus;
import com.eigtheenthstreet.order_service.domain.repository.OrderItemRepository;
import com.eigtheenthstreet.order_service.domain.repository.OrderRepository;
import com.eigtheenthstreet.order_service.domain.service.OrderDomainService;
import com.eigtheenthstreet.order_service.exception.company.CustomCompanyGetApiFailException;
import com.eigtheenthstreet.order_service.exception.company.CustomCompanyNotReceiverException;
import com.eigtheenthstreet.order_service.exception.company.CustomCompanyNotSupplierException;
import com.eigtheenthstreet.order_service.exception.order.CustomOrderDeleteNotAllowedException;
import com.eigtheenthstreet.order_service.exception.order.CustomOrderStatusUpdateNotAllowedException;
import com.eigtheenthstreet.order_service.exception.order.CustomProductNotForSaleException;
import com.eigtheenthstreet.order_service.exception.order.CustomProductRestoreStockApiFailException;
import com.eigtheenthstreet.order_service.exception.order.OrderCancelNotAllowedException;
import com.eigtheenthstreet.order_service.exception.product.CustomInsufficientStockException;
import com.eigtheenthstreet.order_service.exception.product.CustomProductBulkNotFoundException;
import com.eigtheenthstreet.order_service.exception.product.CustomProductDecreaseStockApiFailException;
import com.eigtheenthstreet.order_service.infrastructure.client.CompanyServiceClient;
import com.eigtheenthstreet.order_service.infrastructure.client.ProductServiceClient;
import com.eigtheenthstreet.order_service.infrastructure.client.dto.CreateCompanyResponse;
import com.eigtheenthstreet.order_service.infrastructure.client.dto.GetBulkProductRequest;
import com.eigtheenthstreet.order_service.infrastructure.client.dto.GetBulkProductResponse;
import com.eigtheenthstreet.order_service.infrastructure.messaging.OrderMessageProducer;
import com.eigtheenthstreet.order_service.presentation.request.CreateOrderRequest;
import com.eigtheenthstreet.order_service.presentation.request.UpdateOrderRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final CompanyServiceClient companyServiceClient;
	private final ProductServiceClient productServiceClient;
	private final OrderItemRepository orderItemRepository;
	private final OrderMessageProducer orderMessageProducer;
	private final OrderDomainService orderDomainService;

	@Transactional
	public CreateOrderResponse registerOrder(
		CreateOrderRequest request,
		UUID userId
	) {
		// 업체 정보 조회
		CreateCompanyResponse supplierCompany = getCompany(request.supplierCompanyId());
		CreateCompanyResponse receiverCompany = getCompany(request.consumerCompanyId());
		validateCompanies(supplierCompany, receiverCompany);

		// Order 생성
		Order order = orderDomainService.createOrder(request, userId);

		// 상품 정보 조회 (bulk api)
		List<UUID> productIds = request.orderItems().stream()
			.map(CreateOrderRequest.OrderItemReqeust::productId)
			.toList();

		List<GetBulkProductResponse> bulkProductInfo = getBulkProductInfo(productIds);

		// 주문 상품 목록 생성
		List<OrderItem> orderItems = new ArrayList<>();

		for (CreateOrderRequest.OrderItemReqeust orderItemRequest : request.orderItems()) {
			// 상품 정보를 조회하여 상품 가격 및 재고 확인
			GetBulkProductResponse productResponse = bulkProductInfo.stream()
				.filter(p -> p.productId().equals(orderItemRequest.productId()))
				.findFirst()
				.orElseThrow(() -> new CustomProductBulkNotFoundException(ErrorCode.PRODUCT_BULK_NOT_FOUND));

			// 판매 여부 및 재고 수량 확인
			validateProduct(productResponse, orderItemRequest.productQuantity());

			OrderItem savedOrderItem = orderDomainService.createOrderItem(
				order.getId(),
				orderItemRequest.productId(),
				orderItemRequest.productQuantity(),
				productResponse.productPrice()
			);

			orderItems.add(savedOrderItem);
		}

		// 주문 항목 저장 및 재고 차감
		decreaseTotalProductQuantity(orderItems);

		// 주문 총 가격과 총 수량 수정
		orderDomainService.updateOrderQuantityAndTotalAmount(order.getId(), orderItems);

		// 주문 성공 이벤트 배송으로 발행
		log.info("OrderService에서 주문 생성 성공 이벤트 deliveryQueue로 발행");
		orderMessageProducer.sendToDelivery(order, supplierCompany.hubId(), receiverCompany.hubId());

		// 주문 성공 이벤트 슬랙으로 발행
		log.info("OrderService에서 주문 생성 성공 이벤트 slackQueue로 발행");
		orderMessageProducer.sendToSlack(order);

		return CreateOrderResponse.from(order.getId());
	}

	@Transactional
	public UpdateOrderResponse updateOrder(UpdateOrderRequest request, UUID orderId) {
		// 주문 조회
		Order foundOrder = orderDomainService.getOrderById(orderId);

		// 주문 상태 확인
		if (OrderStatus.isUpdateOrderStatusNotAllowed(foundOrder.getOrderStatus())) {
			throw new CustomOrderStatusUpdateNotAllowedException(ErrorCode.ORDER_STATUS_UPDATE_NOT_ALLOWED);
		}

		// 기존 주문 상품 목록 조회
		List<OrderItem> foundOrderItems = foundOrder.getOrderItems();

		// 요청된 상품 ID 목록
		List<UUID> requestedProductIds = request.orderItems().stream()
			.map(UpdateOrderRequest.UpdateOrderItemRequest::productId)
			.toList();

		// 상품 정보 조회
		List<GetBulkProductResponse> bulkProductInfo = getBulkProductInfo(requestedProductIds);

		// 재고 변경 사항 추적
		Map<UUID, Integer> stockChanges = new HashMap<>();

		for (UpdateOrderRequest.UpdateOrderItemRequest orderItemRequest : request.orderItems()) {
			// 상품 정보를 조회하여 상품 가격 및 재고 확인
			GetBulkProductResponse productResponse = bulkProductInfo.stream()
				.filter(p -> p.productId().equals(orderItemRequest.productId()))
				.findFirst()
				.orElseThrow(() -> new CustomProductBulkNotFoundException(ErrorCode.PRODUCT_BULK_NOT_FOUND));

			// 상품 재고 검증
			validateProduct(productResponse, orderItemRequest.productQuantity());

			// 주문 업데이트
			int quantityDifference = orderDomainService.updateOrderItem(
				orderId,
				orderItemRequest.productId(),
				orderItemRequest.productQuantity(),
				productResponse.productPrice()
			);

			// 재고 차감 또는 복원
			stockChanges.put(orderItemRequest.productId(), quantityDifference);
		}

		// 재고 조정
		for (Map.Entry<UUID, Integer> entry : stockChanges.entrySet()) {
			Integer quantityDifference = entry.getValue();
			UUID productId = entry.getKey();

			if (quantityDifference < 0) {
				// 수량 증가 -> 재고 감소
				decreaseProductQuantity(productId, Math.abs(quantityDifference));
			} else if (quantityDifference > 0) {
				// 수량 감소 -> 재고 복원
				restoreProductQuantity(productId, quantityDifference);
			}

			// 변경이 없는 경우 pass
		}

		// 응답 생성
		List<UpdateOrderResponse.UpdateOrderItemResponse> updateOrderItems = foundOrderItems.stream()
			.map(UpdateOrderResponse.UpdateOrderItemResponse::from)
			.toList();

		return UpdateOrderResponse.of(foundOrder, updateOrderItems);
	}

	@Transactional
	public void deleteOrder(UUID orderId) {
		// 주문 조회
		Order foundOrder = orderDomainService.getOrderById(orderId);

		// 주문 삭제 가능 여부 검증
		if (OrderStatus.isDeleteOrderStatusNotAllowed(foundOrder.getOrderStatus())) {
			throw new CustomOrderDeleteNotAllowedException(ErrorCode.ORDER_DELETE_NOT_ALLOWED);
		}

		// 주문 상품 목록 조회
		List<OrderItem> foundOrderItems = orderDomainService.getOrderItems(orderId);

		// 주문 상품 삭제
		for (OrderItem foundOrderItem : foundOrderItems) {
			foundOrderItem.performSoftDelete();
			// todo. 재고 복구 로직 추가
		}

		// 주문 삭제
		foundOrder.performSoftDelete();
	}

	@Transactional
	public void cancelOrder(UUID orderId) {
		// 주문 조회
		Order foundOrder = orderDomainService.getOrderById(orderId);

		// 주문 취소 가능 여부 검증
		if (OrderStatus.isCancelOrderStatusNotAllowed(foundOrder.getOrderStatus())) {
			throw new OrderCancelNotAllowedException(ErrorCode.ORDER_CANCEL_NOT_ALLOWED);
		}

		// 주문 항목 조회
		List<OrderItem> foundOrderItems = orderDomainService.getOrderItems(orderId);

		// 주문 상품 삭제
		for (OrderItem foundOrderItem : foundOrderItems) {
			foundOrderItem.performSoftDelete();
			// todo. 재고 복구 로직 추가
		}

		// 주문 상태 변경
		foundOrder.cancel();
	}

	@Transactional(readOnly = true)
	public SelectOrderResponse getOrder(UUID orderId) {
		// 주문 조회
		Order foundOrder = orderDomainService.getOrderById(orderId);

		// 주문 상품 조회
		List<OrderItem> orderItems = orderDomainService.getOrderItems(orderId);

		// 주문 상품 응답 dto 생성
		List<SelectOrderResponse.SelectOrderItemResponse> orderItemsDto = new ArrayList<>();

		List<UUID> productIds = orderItems.stream()
			.map(OrderItem::getProductId)
			.toList();

		// 주문 상품 bulk 조회
		List<GetBulkProductResponse> bulkProducts = getBulkProductInfo(productIds);

		for (OrderItem orderItem : orderItems) {
			GetBulkProductResponse productResponse = bulkProducts.stream()
				.filter(product -> product.productId().equals(orderItem.getProductId()))
				.findFirst()
				.orElseThrow(() -> new CustomProductBulkNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

			SelectOrderResponse.SelectOrderItemResponse selectOrderItemResponse = SelectOrderResponse.SelectOrderItemResponse.from(
				productResponse,
				orderItem
			);

			orderItemsDto.add(selectOrderItemResponse);
		}

		// todo. 배송 상태 조회
		//DeliveryResponse delivery = deliveryServiceFeignClient.getDeliveryInfo(orderId);

		return SelectOrderResponse.from(foundOrder, orderItemsDto);
	}

	@Transactional(readOnly = true)
	public PagedModel<SelectOrderResponse> getAllOrders(String query, Pageable pageable) {
		Page<Order> orders = orderRepository.searchByOrders(pageable);
		// 주문 응답 리스트 생성
		// 주문 상품 응답 dto 생성
		List<SelectOrderResponse> content = orders.stream().map(order -> {
			List<OrderItem> orderItems = order.getOrderItems();

			// 주문 상품 ID 목록
			List<UUID> productIds = orderItems.stream()
				.map(OrderItem::getProductId)
				.toList();

			// 상품 정보 한 번만 조회하기 todo. 캐시 사용
			List<GetBulkProductResponse> bulkProducts = getBulkProductInfo(productIds);

			// 각 orderItem의 응답 생성
			List<SelectOrderResponse.SelectOrderItemResponse> orderItemResponse = orderItems.stream()
				.map(orderItem -> {
					// 해당 주문 아이템에 맞는 상품 정보 찾아서 매핑
					GetBulkProductResponse productResponse = bulkProducts.stream()
						.filter(product -> product.productId().equals(orderItem.getProductId()))
						.findFirst()
						.orElseThrow(() -> new CustomProductBulkNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

					return SelectOrderResponse.SelectOrderItemResponse.from(productResponse, orderItem);
				}).toList();

			// 주문 응답 생성
			return SelectOrderResponse.from(order, orderItemResponse);
		}).toList();

		Page<SelectOrderResponse> pageResult = new PageImpl<>(
			content,
			pageable,
			orders.getTotalElements()
		);

		return new PagedModel<>(pageResult);
	}

	private CreateCompanyResponse getCompany(UUID companyId) {
		try {
			return companyServiceClient.getCompany(companyId);
		} catch (Exception e) {
			log.error("업체 조회 API 호출 실패: {}", e.getMessage());
			throw new CustomCompanyGetApiFailException(ErrorCode.ORDER_COMPANY_GET_API_FAIL);
		}
	}

	private void validateCompanies(CreateCompanyResponse supplierCompany, CreateCompanyResponse receiverCompany) {
		if (!CompanyType.SUPPLIER.getName().equals(supplierCompany.companyType())) {
			throw new CustomCompanyNotSupplierException(ErrorCode.ORDER_COMPANY_NOT_SUPPLIER);
		}

		if (!CompanyType.RECEIVER.getName().equals(receiverCompany.companyType())) {
			throw new CustomCompanyNotReceiverException(ErrorCode.ORDER_COMPANY_NOT_RECEIVER);
		}
	}

	private void decreaseTotalProductQuantity(List<OrderItem> orderItems) {
		for (OrderItem orderItem : orderItems) {
			try {
				productServiceClient.decreaseStock(orderItem.getProductId(), orderItem.getQuantity());
			} catch (Exception e) {
				log.error("상품 재고 차감 API 요청 실패: {}", e.getMessage());
				throw new CustomProductDecreaseStockApiFailException(ErrorCode.ORDER_PRODUCT_DECREASE_STOCK_FAIL);
			}
		}
	}

	// 상품 정보를 Bulk API에서 조회하는 메서드
	private List<GetBulkProductResponse> getBulkProductInfo(List<UUID> productIds) {
		GetBulkProductRequest request = GetBulkProductRequest.from(productIds);

		try {
			List<GetBulkProductResponse> bulkProducts = productServiceClient.getBulkProducts(request);
			for (GetBulkProductResponse bulkProduct : bulkProducts) {
				System.out.println("bulkProduct.productId() = " + bulkProduct.productId());
				System.out.println("bulkProduct.productName() = " + bulkProduct.productName());
			}
			return bulkProducts;
		} catch (Exception e) {
			log.error("상품 정보 조회(Bulk API) 실패: {}", e.getMessage());
			throw new CustomProductBulkNotFoundException(ErrorCode.PRODUCT_BULK_NOT_FOUND);
		}
	}

	private void validateProduct(GetBulkProductResponse product, int requestedQuantity) {
		if (!product.isSold()) {
			throw new CustomProductNotForSaleException(ErrorCode.ORDER_PRODUCT_NOT_FOR_SALE);
		}

		if (product.productQuantity() < requestedQuantity) {
			throw new CustomInsufficientStockException(ErrorCode.ORDER_INSUFFICIENT_STOCK);
		}
	}

	private List<UUID> getProductIds(
		List<OrderItem> savedOrderItems,
		List<UpdateOrderRequest.UpdateOrderItemRequest> newOrderItems
	) {
		List<UUID> savedProductIds = savedOrderItems.stream()
			.map(OrderItem::getProductId)
			.toList();

		List<UUID> newProductIds = newOrderItems.stream()
			.map(UpdateOrderRequest.UpdateOrderItemRequest::productId)
			.toList();

		List<UUID> productIds = new ArrayList<>();

		productIds.addAll(savedProductIds);
		productIds.addAll(newProductIds);

		return productIds;
	}

	private void decreaseProductQuantity(UUID productId, int quantity) {
		if (quantity <= 0) {
			return;
		}

		try {
			productServiceClient.decreaseStock(productId, quantity);
		} catch (Exception e) {
			log.error("상품 재고 차감 API 요청 실패: {}", e.getMessage());
			throw new CustomProductDecreaseStockApiFailException(ErrorCode.ORDER_PRODUCT_DECREASE_STOCK_FAIL);
		}
	}

	private void restoreProductQuantity(UUID productId, int quantity) {
		if (quantity <= 0) {
			return;
		}

		try {
			productServiceClient.restoreStock(productId, quantity);
		} catch (Exception e) {
			log.error("상품 재고 복원 API 요청 실패: {}", e.getMessage());
			throw new CustomProductRestoreStockApiFailException(ErrorCode.ORDER_PRODUCT_RESTORE_STOCK_FAIL);
		}
	}
}
