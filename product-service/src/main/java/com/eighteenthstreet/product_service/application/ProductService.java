package com.eighteenthstreet.product_service.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eighteenthstreet.product_service.application.dto.CreateProductResponse;
import com.eighteenthstreet.product_service.application.dto.SelectProductResponse;
import com.eighteenthstreet.product_service.application.dto.UpdateProductResponse;
import com.eighteenthstreet.product_service.domain.model.Product;
import com.eighteenthstreet.product_service.domain.repository.ProductRepository;
import com.eighteenthstreet.product_service.exception.CustomProductAlreadyExistException;
import com.eighteenthstreet.product_service.exception.CustomProductNotFoundException;
import com.eighteenthstreet.product_service.presentation.request.CreateProductRequest;
import com.eighteenthstreet.product_service.presentation.request.UpdateProductRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	//private final HubServiceClient hubServiceClient;
	//private final CompanyServiceClient companyServiceClient;

	@Transactional
	public CreateProductResponse registerProduct(CreateProductRequest request) {
		// todo.허브 검사 -> 허브 존재 여부 확인 => 상품 관리 허브ID는 업체 엔티티에서 관리하므로 업체 검사
		// 1. 업체 정보 조회
		// SelectCompanyResponse company = companyServiceClient.getCompany(foundProduct.getCompanyId());

		// 2. 요청된 hubId와 업체의 hubId 비교
		// if (!company.getHubId().equals(request.hubId())) {
		// 	throw new IllegalArgumentException()
		// }

		// 3. 상품을 생성할 수 있는 유저 권한 체크 (마스터, 허브 관리자, 업체 관리자)

		// 4. 상품 존재 여부 확인 (중복 상품 검열)
		if (productRepository.existsByName(request.name())) {
			throw new CustomProductAlreadyExistException(ErrorCode.PRODUCT_ALREADY_EXIST);
		}

		// 상품 생성
		Product product = Product.create(request);

		// 상품 DB에 저장
		productRepository.save(product);

		// 응답 반환
		return CreateProductResponse.from(product);

	}

	@Transactional
	public UpdateProductResponse updateProduct(UUID productId, UpdateProductRequest request) {
		// id로 등록된 상품 조회
		Product foundProduct = productRepository.findById(productId)
			.orElseThrow(() -> new CustomProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

		// 상품을 수정할 수 있는 유저 권한 체크 (마스터, 허브 관리자, 업체 관리자)
		// - 해당 상품이 속한 회사 정보 가져오기
		//SelectCompanyResponse company = companyServiceClient.getCompany(foundProduct.getCompanyId());

		// if (!company.getManagerId().equals(authenticatedUserId)) {
		// 	throw new UnauthorizedAccessException("해당 권한자는 등록된 상품을 수정할 수 없습니다.");
		// }

		// 상품 수정
		foundProduct.update(request);

		// 응답 반환
		return UpdateProductResponse.from(foundProduct);

	}

	@Transactional
	public void deleteProduct(UUID productId) {
		// id로 등록된 상품 조회
		Product foundProduct = productRepository.findById(productId)
			.orElseThrow(() -> new CustomProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

		// 상품을 삭제할 수 있는 유저 권한 체크 (마스터, 허브 관리자)
		// - 해당 상품이 속한 회사 정보 가져오기
		//SelectCompanyResponse company = companyServiceClient.getCompany(foundProduct.getCompanyId());

		// if (!company.getManagerId().equals(authenticatedUserId)) {
		// 	throw new UnauthorizedAccessException("해당 권한자는 등록된 상품을 삭제할 수 없습니다.");
		// }

		// 상품 soft delete
		foundProduct.performSoftDelete();
	}

	@Transactional(readOnly = true)
	public SelectProductResponse getProduct(UUID productId) {
		// id로 등록된 상품 조회
		Product foundProduct = productRepository.findById(productId)
			.orElseThrow(() -> new CustomProductNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

		// 응답 반환
		return SelectProductResponse.from(foundProduct);
	}

	@Transactional(readOnly = true)
	public PagedModel<SelectProductResponse> getAllProducts(String query, PageRequest pageable) {
		//todo.허브 검사 -> 허브 존재 여부 확인 => 상품 관리 허브ID는 업체 엔티티에서 관리하므로 업체 검사
		//1. 업체 정보 조회
		//SelectCompanyResponse company = companyServiceClient.getCompany(foundProduct.getCompanyId());

		//2. 요청된 hubId와 업체의 hubId 비교
		//if (!company.getHubId().equals(request.hubId())) {
		//	throw new IllegalArgumentException()
		//}

		// 상품 조회 -> 특정 허브와 특정 업체에 해당하는 상품 목록 조회
		Page<Product> products = productRepository.searchByProducts(query, pageable);

		Page<SelectProductResponse> content = products.map(SelectProductResponse::from);

		// 응답 데이터 PagedModel로 변환
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
}
