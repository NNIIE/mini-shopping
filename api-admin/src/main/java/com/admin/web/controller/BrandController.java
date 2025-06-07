package com.admin.web.controller;

import com.admin.service.BrandService;
import com.admin.web.request.CreateBrandRequest;
import com.admin.web.response.CreateBrandResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/brand")
public class BrandController {

    private final BrandService brandService;

    @PostMapping()
    public ResponseEntity<CreateBrandResponse> createBrand(
        @RequestBody @Valid final CreateBrandRequest createBrandRequest
    ) {
        brandService.createBrand(createBrandRequest);

        return null;
    }

}
