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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eighteenthstreet.product_service.application.ProductService;
import com.eighteenthstreet.product_service.application.dto.CreateProductResponse;
import com.eighteenthstreet.product_service.application.dto.SelectProductResponse;
import com.eighteenthstreet.product_service.application.dto.UpdateProductResponse;
import com.eighteenthstreet.product_service.exception.CustomProductRoleDeniedException;
import com.eighteenthstreet.product_service.presentation.request.CreateProductRequest;
import com.eighteenthstreet.product_service.presentation.request.SearchCondition;
import com.eighteenthstreet.product_service.presentation.request.UpdateProductRequest;

import auth.JwtUtil;
import auth.Role;
import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
	private final JwtUtil jwtUtil;
	private final ProductService productService;

	@PostMapping()
	public ResponseEntity<CreateProductResponse> registerProduct(
		@RequestBody CreateProductRequest request,
		@RequestHeader("Authorization") String token
	) {
		Role role = jwtUtil.getRoleFromToken(token);
		hasValidRegisterRole(role);

		CreateProductResponse response = productService.registerProduct(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PatchMapping("/{productId}")
	public ResponseEntity<UpdateProductResponse> updateProduct(
		@PathVariable UUID productId,
		@RequestBody UpdateProductRequest request,
		@RequestHeader("Authorization") String token
	) {
		Role role = jwtUtil.getRoleFromToken(token);
		hasValidUpdateRole(role);

		UpdateProductResponse response = productService.updateProduct(productId, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> deleteProduct(
		@PathVariable UUID productId,
		@RequestHeader("Authorization") String token
	) {
		Role role = jwtUtil.getRoleFromToken(token);
		hasValidDeleteRole(role);

		productService.deleteProduct(productId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{productId}")
	public ResponseEntity<SelectProductResponse> getProduct(@PathVariable UUID productId) {
		SelectProductResponse response = productService.getProduct(productId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

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

	@PutMapping("/{productId}/stock/decrease")
	public ResponseEntity<Void> decreaseStock(
		@PathVariable UUID productId,
		@RequestParam int quantity
	) {
		productService.decreaseStock(productId, quantity);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/{productId}/stock/restore")
	public ResponseEntity<Void> restoreStock(
		@PathVariable UUID productId,
		@RequestParam int quantity
	) {
		productService.restoreStock(productId, quantity);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	private void hasValidRegisterRole(Role role) {
		if (role == null || role.equals(Role.DELIVERY)) {
			throw new CustomProductRoleDeniedException(ErrorCode.PRODUCT_POST_ROLE_DENIED);
		}
	}

	private void hasValidUpdateRole(Role role) {
		if (role == null || role.equals(Role.DELIVERY)) {
			throw new CustomProductRoleDeniedException(ErrorCode.PRODUCT_UPDATE_ROLE_DENIED);
		}
	}

	private void hasValidDeleteRole(Role role) {
		if (role == null || !role.equals(Role.MASTER) && !role.equals(Role.HUB)) {
			throw new CustomProductRoleDeniedException(ErrorCode.PRODUCT_DELETE_ROLE_DENIED);
		}
	}
}
