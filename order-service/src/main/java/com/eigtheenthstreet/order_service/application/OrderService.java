package com.eigtheenthstreet.order_service.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eigtheenthstreet.order_service.application.dto.CompanyType;
import com.eigtheenthstreet.order_service.application.dto.CreateCompanyResponse;
import com.eigtheenthstreet.order_service.application.dto.CreateOrderResponse;
import com.eigtheenthstreet.order_service.application.dto.CreateProductResponse;
import com.eigtheenthstreet.order_service.application.dto.SelectOrderResponse;
import com.eigtheenthstreet.order_service.application.dto.UpdateOrderResponse;
import com.eigtheenthstreet.order_service.domain.model.Order;
import com.eigtheenthstreet.order_service.domain.model.OrderItem;
import com.eigtheenthstreet.order_service.domain.model.OrderStatus;
import com.eigtheenthstreet.order_service.domain.repository.OrderItemRepository;
import com.eigtheenthstreet.order_service.domain.repository.OrderRepository;
import com.eigtheenthstreet.order_service.exception.CustomCompanyNotReceiverException;
import com.eigtheenthstreet.order_service.exception.CustomCompanyNotSupplierException;
import com.eigtheenthstreet.order_service.exception.CustomInsufficientStockException;
import com.eigtheenthstreet.order_service.exception.CustomOrderDeleteNotAllowedException;
import com.eigtheenthstreet.order_service.exception.CustomOrderNotFoundException;
import com.eigtheenthstreet.order_service.exception.CustomOrderStatusUpdateNotAllowedException;
import com.eigtheenthstreet.order_service.exception.CustomProductNotForSaleException;
import com.eigtheenthstreet.order_service.exception.OrderCancelNotAllowedException;
import com.eigtheenthstreet.order_service.infrastructure.client.CompanyServiceClient;
import com.eigtheenthstreet.order_service.infrastructure.client.ProductServiceClient;
import com.eigtheenthstreet.order_service.presentation.request.CreateOrderRequest;
import com.eigtheenthstreet.order_service.presentation.request.UpdateOrderRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final CompanyServiceClient companyServiceClient;
	private final ProductServiceClient productServiceClient;
	//private final DeliveryServiceClient deliveryServiceClient;
	private final OrderItemRepository orderItemRepository;

	@Transactional
	public CreateOrderResponse registerOrder(CreateOrderRequest request, Long userId) {
		// 업체 검사
		CreateCompanyResponse supplierCompany = companyServiceClient.getCompany(request.supplierCompanyId());
		CreateCompanyResponse consumerCompany = companyServiceClient.getCompany(request.consumerCompanyId());

		if (!CompanyType.SUPPLIER.getName().equals(supplierCompany.companyType())) {
			throw new CustomCompanyNotSupplierException(ErrorCode.ORDER_COMPANY_NOT_SUPPLIER);
		}

		if (!CompanyType.RECEIVER.getName().equals(consumerCompany.companyType())) {
			throw new CustomCompanyNotReceiverException(ErrorCode.ORDER_COMPANY_NOT_RECEIVER);
		}

		// Order 생성
		Order order = Order.create(request, userId);
		orderRepository.save(order);

		// 주문 항목 생성 및 총액 계산
		int totalQuantity = 0;
		int totalAmount = 0;

		for (CreateOrderRequest.OrderItemReqeust orderItem : request.orderItems()) {
			// 상품 검사 -> 판매 상태, 가격 변동 여부, 재고 수량 확인
			CreateProductResponse product = productServiceClient.getProduct(orderItem.productId());

			if (!product.isSold()) {
				throw new CustomProductNotForSaleException(ErrorCode.ORDER_PRODUCT_NOT_FOR_SALE);

			}

			if (product.stockQuantity() < orderItem.productQuantity()) {
				throw new CustomInsufficientStockException(ErrorCode.ORDER_INSUFFICIENT_STOCK);
			}

			// 주문 항목 생성
			OrderItem savedOrderItem = OrderItem.create(
				order.getId(),
				orderItem.productId(),
				orderItem.productQuantity(),
				product.productPrice());

			orderItemRepository.save(savedOrderItem);

			// 총 수량과 총액 계산
			totalQuantity += savedOrderItem.getQuantity();
			totalAmount += savedOrderItem.getTotalPrice();

			// 재고 차감 요청
			//productServiceClient.decreaseStock(orderItem.productId(), orderItem.productQuantity());
		}

		// Order에 총 수량과 총액 설정
		order.addOrderTotalQuantityAndTotalAmount(totalQuantity, totalAmount);

		// 배송 및 배송 경로 생성
		// todo. 주문이 생성될 때 배송과 배송 경로가 생성되어야 한다
		// 	주문에서 deliveryServiceClient 호출 -> delivery-service에서 delivery랑 deliveryRoute 생성 -> order-service에서 deliveryId만 받아와서 order에 연결
		// try {
		// 	CreateDeliveryResponse delivery = deliveryServiceClient.createDelivery(
		// 		order.getId(),
		// 		request.deliveryAddress()
		// 	);
		//
		// 	order.addDelivery(delivery.deliveryId());
		// } catch (Exception e) {
		// 	order.changeOrderStatusFailed();
		// }

		// 응답 반환
		return CreateOrderResponse.from(order.getId());
	}

	@Transactional
	public UpdateOrderResponse updateOrder(UpdateOrderRequest request, UUID orderId) {
		// 주문 조회
		Order foundOrder = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomOrderNotFoundException(ErrorCode.ORDER_NOT_FOUND));

		// 주문 상태 확인
		if (OrderStatus.isUpdateOrderStatusNotAllowed(foundOrder.getOrderStatus())) {
			throw new CustomOrderStatusUpdateNotAllowedException(ErrorCode.ORDER_STATUS_UPDATE_NOT_ALLOWED);
		}

		// 기존 주문 상품 목록 조회
		List<OrderItem> foundOrderItems = orderItemRepository.findByOrderId(orderId);

		// 변경된 주문 상품 목록 조회
		List<OrderItem> updatedOrderItems = new ArrayList<>();

		// 변경된 주문 항목을 ProductServiceClient로 전달하여 추가 상품 재고 확인
		List<UpdateOrderRequest.UpdateOrderItemRequest> orderItemsRequest = request.orderItems();
		for (UpdateOrderRequest.UpdateOrderItemRequest orderItemRequest : orderItemsRequest) {
			// 추가 상품 조회
			CreateProductResponse productClientResponse = productServiceClient.getProduct(orderItemRequest.productId());

			// 상품 검사
			if (!productClientResponse.isSold()) {
				throw new CustomProductNotForSaleException(ErrorCode.ORDER_PRODUCT_NOT_FOR_SALE);
			}

			if (productClientResponse.stockQuantity() < orderItemRequest.productQuantity()) {
				throw new CustomInsufficientStockException(ErrorCode.ORDER_INSUFFICIENT_STOCK);
			}

			// 변경된 주문 상품 목록 생성
			// 기존 주문 상품 중 수정할 상품 조회
			Optional<OrderItem> existingOrderItem = foundOrderItems.stream()
				.filter(orderItem -> orderItem.getProductId().equals(orderItemRequest.productId()))
				.findFirst();

			// 주문 상품 수정
			if (existingOrderItem.isPresent()) {
				OrderItem orderItem = existingOrderItem.get();
				orderItem.update(orderItemRequest, productClientResponse.productPrice());
				updatedOrderItems.add(orderItem);
			} else {
				// 기존에 없는 주문 상품 새로 추가
				OrderItem orderItem = OrderItem.create(
					foundOrder.getId(),
					orderItemRequest.productId(),
					orderItemRequest.productQuantity(),
					productClientResponse.productPrice()
				);
				updatedOrderItems.add(orderItem);
			}

			// 재고 차감 요청
			// 재고가 충분한 경우 수량 차감, 재고 부족 시 주문 수정 불가 처리
			//productServiceClient.decreaseStock(orderItem.productId(), orderItem.productQuantity());
		}

		//orderItemRepository.saveAll(updatedOrderItems); //-> saveAll 오류 발생
		updatedOrderItems.forEach(orderItemRepository::save);

		// order의 수량과 가격 업데이트
		foundOrder.updateOrderQuantityAndTotalPrice(updatedOrderItems);

		List<UpdateOrderResponse.UpdateOrderItemResponse> updateOrderItems = updatedOrderItems.stream()
			.map(UpdateOrderResponse.UpdateOrderItemResponse::from)
			.toList();

		return UpdateOrderResponse.of(foundOrder, updateOrderItems);
	}

	@Transactional
	public void deleteOrder(UUID orderId, Long userId) {
		// todo. 주문 삭제 권한 확인

		// 주문 조회
		Order foundOrder = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomOrderNotFoundException(ErrorCode.ORDER_NOT_FOUND));

		// 주문 상태 확인
		if (OrderStatus.isDeleteOrderStatusNotAllowed(foundOrder.getOrderStatus())) {
			throw new CustomOrderDeleteNotAllowedException(ErrorCode.ORDER_DELETE_NOT_ALLOWED);
		}

		// 주문 상품 목록 조회
		List<OrderItem> foundOrderItems = orderItemRepository.findByOrderId(orderId);

		// 주문 상품 삭제
		for (OrderItem foundOrderItem : foundOrderItems) {
			foundOrderItem.performSoftDelete();

			// try {
			// 	//productServiceClient.restoreStock(foundOrderItem.getProductId(), foundOrderItem.getQuantity());
			// } catch (Exception e) {
			// 	throw new IllegalStateException("상품 재고 복구에 실패했습니다.", e);
			// }
		}

		// 배송 삭제
		// try {
		// 	deliveryServiceFeignClient.deleteDelivery(orderId); // DeliveryServiceClient 호출
		// } catch (Exception e) {
		// 	throw new IllegalStateException("배송 정보 삭제에 실패했습니다.", e);
		// }

		// 주문 삭제
		foundOrder.performSoftDelete();
	}

	@Transactional
	public void cancelOrder(UUID orderId, Long userId) {
		// 주문 취소 권한

		// 주문 조회
		Order foundOrder = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomOrderNotFoundException(ErrorCode.ORDER_NOT_FOUND));

		if (OrderStatus.isCancelOrderStatusNotAllowed(foundOrder.getOrderStatus())) {
			throw new OrderCancelNotAllowedException(ErrorCode.ORDER_CANCEL_NOT_ALLOWED);
		}

		// 주문 항목 조회
		List<OrderItem> foundOrderItems = orderItemRepository.findByOrderId(orderId);

		// 주문 상품 삭제
		for (OrderItem foundOrderItem : foundOrderItems) {
			foundOrderItem.performSoftDelete();

			// 주문 상품 재고 복구
			// try {
			// 	//productServiceClient.restoreStock(foundOrderItem.getProductId(), foundOrderItem.getQuantity());
			// } catch (Exception e) {
			// 	throw new IllegalStateException("상품 재고 복구에 실패했습니다.", e);
			// }
		}

		// 배송 정보 삭제
		// try {
		// 	deliveryServiceFeignClient.deleteDelivery(orderId); // DeliveryServiceClient 호출
		// } catch (Exception e) {
		// 	throw new IllegalStateException("배송 정보 삭제에 실패했습니다.", e);
		// }

		// 주문 상태 변경
		foundOrder.cancel();
	}

	@Transactional(readOnly = true)
	public SelectOrderResponse getOrder(UUID orderId) {
		// 주문 조회
		Order foundOrder = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomOrderNotFoundException(ErrorCode.ORDER_NOT_FOUND));

		// 주문 상품 조회
		List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

		// 주문 상품 응답 dto 생성
		List<SelectOrderResponse.SelectOrderItemResponse> orderItemsDto = new ArrayList<>();

		for (OrderItem orderItem : orderItems) {
			SelectOrderResponse.SelectOrderItemResponse selectOrderItemResponse = SelectOrderResponse.SelectOrderItemResponse.from(
				orderItem
			);
			orderItemsDto.add(selectOrderItemResponse);
		}

		// 배송 및 배송 경로 정보 조회
		//DeliveryResponse delivery = deliveryServiceFeignClient.getDeliveryInfo(orderId);

		return SelectOrderResponse.from(foundOrder, orderItemsDto);
	}

	@Transactional(readOnly = true)
	public PagedModel<SelectOrderResponse> getAllOrders(String query, Pageable pageable) {
		Page<Order> orders = orderRepository.searchByOrders(pageable);
		// todo. 배송 정보도 함께 조회?
		List<SelectOrderResponse> content = orders.stream().map(order -> {
			List<OrderItem> foundOrderItem = orderItemRepository.findByOrderId(order.getId());

			List<SelectOrderResponse.SelectOrderItemResponse> orderItemsResponse = SelectOrderResponse.SelectOrderItemResponse.of(
				foundOrderItem);

			return SelectOrderResponse.from(order, orderItemsResponse);
		}).toList();

		Page<SelectOrderResponse> pageResult = new PageImpl<>(
			content,
			pageable,
			orders.getTotalElements()
		);

		return new PagedModel<>(pageResult);
	}
}
