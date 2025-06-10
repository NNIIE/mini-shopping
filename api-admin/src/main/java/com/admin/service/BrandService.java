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
        return getBrandById(id);
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

    public Brand getBrandById(final Long id) {
        return brandRepository.findById(id).orElseThrow(
            () -> new BusinessException(ErrorCode.BRAND_NOT_FOUND)
        );
    }

    /**
     * 브랜드 이름이 중복되는지 확인하고, 중복될 경우 예외를 발생시킵니다.
     *
     * @param brandName 중복 여부를 확인할 브랜드 이름
     * @throws BusinessException 브랜드 이름이 이미 존재할 경우 {@code ErrorCode.BRAND_NAME_CONFLICT}와 함께 발생합니다.
     */
    public void checkBrandNameDuplication(final String brandName) {
        brandRepository.findByName(brandName).ifPresent(brand -> {
            throw new BusinessException(ErrorCode.BRAND_NAME_CONFLICT);
        });
    }

}

