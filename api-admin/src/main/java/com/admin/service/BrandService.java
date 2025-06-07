package com.admin.service;

import com.admin.exception.BusinessException;
import com.admin.exception.ErrorCode;
import com.admin.web.request.brand.CreateBrandRequest;
import com.admin.web.request.brand.UpdateBrandRequest;
import com.storage.admin.Admin;
import com.storage.brand.Brand;
import com.storage.brand.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    @Transactional(readOnly = true)
    public Page<Brand> getMyBrands(final Admin admin, final Pageable pageable) {
        return brandRepository.findByAdminId(admin.getId(), pageable);
    }

    @Transactional(readOnly = true)
    public Brand getBrand(final Long id) {
        return brandRepository.findById(id).orElseThrow(
            () -> new BusinessException(ErrorCode.BRAND_NOT_FOUND)
        );
    }

    @Transactional
    public Brand createBrand(final CreateBrandRequest request, final Admin admin) {
        checkBrandNameDuplication(request.getName());
        final Brand brand = Brand.builder()
            .admin(admin)
            .name(request.getName())
            .businessNumber(request.getBusinessNumber())
            .build();

        return brandRepository.save(brand);
    }

    @Transactional
    public Brand updateBrand(final Long id, final UpdateBrandRequest request) {
        checkBrandNameDuplication(request.getName());
        final Brand brand = brandRepository.findById(id).orElseThrow(
            () -> new BusinessException(ErrorCode.BRAND_NOT_FOUND)
        );

        brand.setName(request.getName());
        return brand;
    }

    @Transactional
    public Brand deleteBrand(final Long id, final Admin admin) {
        final Brand brand = brandRepository.findByIdAndAdminId(id, admin.getId())
            .orElseThrow(() -> new BusinessException(ErrorCode.BRAND_NOT_FOUND));

        brandRepository.delete(brand);

        return brand;
    }

    public void checkBrandNameDuplication(final String brandName) {
        brandRepository.findByName(brandName).ifPresent(brand -> {
            throw new BusinessException(ErrorCode.BRAND_NAME_CONFLICT);
        });
    }

}
