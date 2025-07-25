package com.admin.web.controller;

import com.admin.service.ProductService;
import com.admin.web.request.product.CreateProductRequest;
import com.admin.web.request.product.UpdateProductRequest;
import com.admin.web.response.product.ProductResponse;
import com.relation.product.Product;
import com.support.response.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product")
@Tag(name = "Product", description = "Product Management API")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/brand/{id}")
    @Operation(summary = "상품 목록 조회 - 브랜드별")
    public ResponseEntity<PagedResponse<ProductResponse>> getProductsByBrand(
        @PathVariable final Long id,
        @PageableDefault final Pageable pageable
    ) {
        final Page<Product> pages = productService.getProductsByBrand(id, pageable);
        final List<ProductResponse> productResponses = pages.stream()
            .map(product -> new ProductResponse(
                product.getId(),
                product.getBrand().getId(),
                product.getCategory().getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
            )).toList();

        final PagedResponse<ProductResponse> response = new PagedResponse<>(
            productResponses,
            pages.getTotalPages(),
            pages.getTotalElements(),
            pages.getNumber(),
            pages.getSize()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 조회")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable final Long id) {
        final Product product = productService.getProductById(id);
        final ProductResponse response = new ProductResponse(
            product.getId(),
            product.getBrand().getId(),
            product.getCategory().getId(),
            product.getName(),
            product.getPrice(),
            product.getStock()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "상품 생성")
    public ResponseEntity<ProductResponse> createProduct(
        @RequestBody @Valid final CreateProductRequest request
    ) {
        final Product createdProduct = productService.createProduct(request);
        final ProductResponse response = new ProductResponse(
            createdProduct.getId(),
            createdProduct.getBrand().getId(),
            createdProduct.getCategory().getId(),
            createdProduct.getName(),
            createdProduct.getPrice(),
            createdProduct.getStock()
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "상품 수정")
    public ResponseEntity<ProductResponse> updateProduct(
        @PathVariable final Long id,
        @RequestBody @Valid final UpdateProductRequest request
    ) {
        final Product updatedProduct = productService.updateProduct(id, request);
        final ProductResponse response = new ProductResponse(
            updatedProduct.getId(),
            updatedProduct.getBrand().getId(),
            updatedProduct.getCategory().getId(),
            updatedProduct.getName(),
            updatedProduct.getPrice(),
            updatedProduct.getStock()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable final Long id) {
        final Product deletedProduct = productService.deleteProduct(id);
        final ProductResponse response = new ProductResponse(
            deletedProduct.getId(),
            deletedProduct.getBrand().getId(),
            deletedProduct.getCategory().getId(),
            deletedProduct.getName(),
            deletedProduct.getPrice(),
            deletedProduct.getStock()
        );

        return ResponseEntity.ok(response);
    }

}
