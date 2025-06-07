package com.admin.web.controller;

import com.admin.principal.CurrentAdmin;
import com.admin.service.BrandService;
import com.admin.web.request.brand.CreateBrandRequest;
import com.admin.web.request.brand.UpdateBrandRequest;
import com.admin.web.response.brand.BrandResponse;
import com.storage.admin.Admin;
import com.storage.brand.Brand;
import com.support.response.PagedResponse;
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
public class BrandController {

    private final BrandService brandService;

    @GetMapping
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
    public ResponseEntity<BrandResponse> getBrand(@PathVariable final Long id) {
        final Brand brand = brandService.getBrand(id);
        final BrandResponse response = new BrandResponse(
            brand.getId(),
            brand.getName()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
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
