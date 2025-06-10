package com.admin.web.controller;

import com.admin.principal.CurrentAdmin;
import com.admin.service.BrandService;
import com.admin.web.request.brand.CreateBrandRequest;
import com.admin.web.request.brand.UpdateBrandRequest;
import com.admin.web.response.brand.BrandResponse;
import com.storage.admin.Admin;
import com.storage.brand.Brand;
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
@RequestMapping("/admin/brand")
@Tag(name = "Brand", description = "Brand Management API")
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    @Operation(summary = "브랜드 목록 조회")
    public ResponseEntity<PagedResponse<BrandResponse>> getBrands(
        @CurrentAdmin final Admin admin,
        @PageableDefault final Pageable pageable
    ) {
        final Page<Brand> pages = brandService.getMyBrands(admin, pageable);
        final List<BrandResponse> brandResponses = pages.stream()
            .map(brand -> new BrandResponse(brand.getId(), brand.getName()))
            .toList();

        final PagedResponse<BrandResponse> response = new PagedResponse<>(
            brandResponses,
            pages.getTotalPages(),
            pages.getTotalElements(),
            pages.getNumber(),
            pages.getSize()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "브랜드 조회")
    public ResponseEntity<BrandResponse> getBrand(@PathVariable final Long id) {
        final Brand brand = brandService.getBrand(id);
        final BrandResponse response = new BrandResponse(
            brand.getId(),
            brand.getName()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "브랜드 생성")
    public ResponseEntity<BrandResponse> createBrand(
        @RequestBody @Valid final CreateBrandRequest request,
        @CurrentAdmin final Admin admin
    ) {
        final Brand createdBrand = brandService.createBrand(request, admin);
        final BrandResponse response = new BrandResponse(
            createdBrand.getId(),
            createdBrand.getName()
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "브랜드 수정")
    public ResponseEntity<BrandResponse> updateBrand(
        @PathVariable final Long id,
        @RequestBody @Valid final UpdateBrandRequest request
    ) {
        final Brand updatedBrand = brandService.updateBrand(id, request);
        final BrandResponse response = new BrandResponse(
            updatedBrand.getId(),
            updatedBrand.getName()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "브랜드 삭제")
    public ResponseEntity<BrandResponse> deleteBrand(
        @PathVariable final Long id,
        @CurrentAdmin final Admin admin
    ) {
        final Brand deletedBrand = brandService.deleteBrand(id, admin);
        final BrandResponse response = new BrandResponse(
            deletedBrand.getId(),
            deletedBrand.getName()
        );

        return ResponseEntity.ok(response);
    }

}
