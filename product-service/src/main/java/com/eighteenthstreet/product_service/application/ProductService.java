package com.eighteenthstreet.product_service.application;

import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.product_service.application.dto.BulkProductResponse;
import com.eighteenthstreet.product_service.application.dto.BulkProductsResponse;
import com.eighteenthstreet.product_service.application.dto.CreateProductResponse;
import com.eighteenthstreet.product_service.application.dto.SelectCompanyResponse;
import com.eighteenthstreet.product_service.application.dto.SelectProductResponse;
import com.eighteenthstreet.product_service.application.dto.UpdateProductResponse;
import com.eighteenthstreet.product_service.domain.model.Product;
import com.eighteenthstreet.product_service.domain.repository.ProductRepository;
import com.eighteenthstreet.product_service.exception.CustomGetCompanyApiFailException;
import com.eighteenthstreet.product_service.exception.CustomMismatchHubIdException;
import com.eighteenthstreet.product_service.exception.CustomProductAlreadyExistException;
import com.eighteenthstreet.product_service.exception.CustomProductNotFoundException;
import com.eighteenthstreet.product_service.infrastructure.client.CompanyServiceClient;
import com.eighteenthstreet.product_service.presentation.request.BulkProductRequest;
import com.eighteenthstreet.product_service.presentation.request.CreateProductRequest;
import com.eighteenthstreet.product_service.presentation.request.UpdateProductRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	private final CompanyServiceClient companyServiceClient;

	@Transactional
	@CachePut(cacheNames = "productCache", key = "#result.productId()")
	public CreateProductResponse registerProduct(CreateProductRequest request) {
		// 업체 정보 조회
		UUID hubId = null;
		try {
			SelectCompanyResponse companyResponse = companyServiceClient.getCompany(request.companyId());
			hubId = companyResponse.hubId();
		} catch (Exception e) {
			log.error("업체 정보 조회 API 호출 실패: {}", e.getMessage());
			throw new CustomGetCompanyApiFailException(ErrorCode.PRODUCT_NOT_FOUND_COMPANY);
		}

		// 요청된 hubId와 업체의 hubId 비교
		if (!hubId.equals(request.hubId())) {
			throw new CustomMismatchHubIdException(ErrorCode.PRODUCT_MISMATCH_HUB_ID);
		}

		// 상품 존재 여부 확인 (중복 상품 검열)
		if (productRepository.existsByName(request.name())) {
			throw new CustomProductAlreadyExistException(ErrorCode.PRODUCT_ALREADY_EXIST);
		}

		// 상품 생성 및 저장
		Product product = Product.create(request);
		productRepository.save(product);

		// 응답 반환
		return CreateProductResponse.from(product);

	}

	@Transactional
	@CachePut(cacheNames = "productCache", key = "#result.productId", condition = "#result != null")
	@CacheEvict(cacheNames = "productAllCache", allEntries = true)
	public UpdateProductResponse updateProduct(UUID productId, UpdateProductRequest request) {
		// id로 등록된 상품 조회
		Product foundProduct = productRepository.findById(productId)
			.orElseThrow(() -> new CustomProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

		// 상품 수정
		foundProduct.update(request);

		// 응답 반환
		return UpdateProductResponse.from(foundProduct);

	}

	@Transactional
	@CacheEvict(cacheNames = {"productAllCache", "productCache"}, key = "#productId")
	public void deleteProduct(UUID productId) {
		// id로 등록된 상품 조회
		Product foundProduct = productRepository.findById(productId)
			.orElseThrow(() -> new CustomProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

		// 상품 soft delete
		foundProduct.performSoftDelete();
	}

	@Transactional(readOnly = true)
	@Cacheable(cacheNames = "productCache", key = "#productId")
	public SelectProductResponse getProduct(UUID productId) {
		// id로 등록된 상품 조회
		Product foundProduct = productRepository.findById(productId)
			.orElseThrow(() -> new CustomProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

		// 응답 반환
		return SelectProductResponse.from(foundProduct);
	}

	@Transactional(readOnly = true)
	public PagedModel<SelectProductResponse> getAllProducts(String query, PageRequest pageable) {
		// 상품 조회 -> query: 상품명
		Page<Product> products = productRepository.searchByProducts(query, pageable);

		Page<SelectProductResponse> content = products.map(SelectProductResponse::from);

		return new PagedModel<>(content);
	}

	@Transactional
	public void decreaseStock(UUID productId, int quantity) {
		// 상품 조회
		Product foundProduct = productRepository.findById(productId)
			.orElseThrow(() -> new CustomProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

		// 주문 수량 만큼 상품 재고 차감
		foundProduct.decreaseStock(quantity);
	}

	@Transactional
	public void restoreStock(UUID productId, int quantity) {
		// 상품 조회
		Product foundProduct = productRepository.findById(productId)
			.orElseThrow(() -> new CustomProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

		// 상품 재고 복원
		foundProduct.restoreStock(quantity);
	}

	@Cacheable(cacheNames = "productAllCache", key = "getMethodName()")
	public BulkProductsResponse getBulkProducts(BulkProductRequest request) {
		List<UUID> productIds = request.productIds();
		List<Product> products = productRepository.findByIds(productIds);

		List<BulkProductResponse> bulkProducts = products.stream()
			.map(BulkProductResponse::from)
			.toList();

		return new BulkProductsResponse(bulkProducts);
	}
}
