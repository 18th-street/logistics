package com.eighteenthstreet.product_service.presentation;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.product_service.application.ProductService;
import com.eighteenthstreet.product_service.application.dto.BulkProductsResponse;
import com.eighteenthstreet.product_service.application.dto.CreateProductResponse;
import com.eighteenthstreet.product_service.application.dto.SelectProductResponse;
import com.eighteenthstreet.product_service.application.dto.UpdateProductResponse;
import com.eighteenthstreet.product_service.presentation.request.BulkProductRequest;
import com.eighteenthstreet.product_service.presentation.request.CreateProductRequest;
import com.eighteenthstreet.product_service.presentation.request.SearchCondition;
import com.eighteenthstreet.product_service.presentation.request.UpdateProductRequest;

import auth.CheckRole;
import auth.Role;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
	private final ProductService productService;

	@Operation(summary = "상품 등록", description = "마스터 관리자, 허브 관리자, 배송담당자는 상품을 등록할 수 있습니다.")
	@CheckRole({Role.MASTER, Role.HUB, Role.COMPANY})
	@PostMapping()
	public ResponseEntity<CreateProductResponse> registerProduct(@RequestBody CreateProductRequest request) {
		CreateProductResponse response = productService.registerProduct(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "상품 수정", description = "마스터 관리자, 허브 관리자, 배송담당자는 상품을 수정할 수 있습니다.")
	@CheckRole({Role.MASTER, Role.HUB, Role.COMPANY})
	@PatchMapping("/{productId}")
	public ResponseEntity<UpdateProductResponse> updateProduct(
		@PathVariable UUID productId,
		@RequestBody UpdateProductRequest request
	) {
		UpdateProductResponse response = productService.updateProduct(productId, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(summary = "상품 삭제", description = "마스터 관리자, 허브 관리자는 상품을 삭제할 수 있습니다.")
	@CheckRole({Role.MASTER, Role.HUB})
	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
		productService.deleteProduct(productId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(summary = "상품 조회", description = "모든 사용자는 상품을 조회할 수 있습니다.")
	@GetMapping("/{productId}")
	public ResponseEntity<SelectProductResponse> getProduct(@PathVariable UUID productId) {
		SelectProductResponse response = productService.getProduct(productId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(summary = "상품 검색", description = "모든 사용자는 상품을 검색할 수 있습니다.")
	@GetMapping()
	public ResponseEntity<PagedModel<SelectProductResponse>> getAllProducts(
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size,
		@RequestParam(name = "sort", defaultValue = "createdAt") String sort,
		@RequestParam(name = "q", required = false) String query
	) {
		SearchCondition searchCondition = SearchCondition.of(
			String.valueOf(page),
			String.valueOf(size),
			sort,
			query
		);

		PageRequest pageable = PageRequest.of(
			searchCondition.getPage(),
			searchCondition.getSize(),
			searchCondition.getSort().getSort()
		);

		PagedModel<SelectProductResponse> response = productService.getAllProducts(query, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(summary = "상품 재고 차감", description = "상품 ID 목록을 전달하여 여러 개의 상품 정보를 한 번에 조회하는 API입니다.")
	@PutMapping("/{productId}/stock/decrease")
	public ResponseEntity<Void> decreaseStock(
		@PathVariable UUID productId,
		@RequestParam int quantity
	) {
		productService.decreaseStock(productId, quantity);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(summary = "상품 재고 복원", description = "주문 취소 시 상품의 재고를 원래 상태로 복원하는 API입니다")
	@PutMapping("/{productId}/stock/restore")
	public ResponseEntity<Void> restoreStock(
		@PathVariable UUID productId,
		@RequestParam int quantity
	) {
		productService.restoreStock(productId, quantity);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(summary = "상품 Bulk 조회", description = "상품 ID 목록으로 한 번에 여러 개의 상품을 조회할 수 있습니다.")
	@PostMapping("/bulk")
	public ResponseEntity<BulkProductsResponse> getBulkProducts(@RequestBody BulkProductRequest request) {
		BulkProductsResponse response = productService.getBulkProducts(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
