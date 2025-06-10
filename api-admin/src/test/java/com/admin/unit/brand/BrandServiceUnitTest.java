package com.admin.unit.brand;

import com.admin.fixture.AdminFixture;
import com.admin.fixture.BrandFixture;
import com.admin.service.BrandService;
import com.admin.web.request.brand.CreateBrandRequest;
import com.admin.web.request.brand.UpdateBrandRequest;
import com.storage.admin.Admin;
import com.storage.brand.Brand;
import com.storage.brand.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class BrandServiceUnitTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    private Admin mockAdmin;
    private Brand mockBrand;

    private final String brandA = "브랜드A";
    private final String businessNumber = "123-45-67890";

    @BeforeEach
    void setUp() {
        mockAdmin = AdminFixture.createAdminWithAnId();
        mockBrand = BrandFixture.createBrand(mockAdmin, brandA, businessNumber);
    }

    @Test
    @DisplayName("브랜드 생성 성공")
    void createBrand_success() {
        // given
        CreateBrandRequest request = BrandFixture.createRequestForCreateBrand(brandA, businessNumber);
        given(brandRepository.findByName(request.getName())).willReturn(Optional.empty());
        given(brandRepository.save(any(Brand.class))).willReturn(mockBrand);

        // when
        Brand result = brandService.createBrand(request, mockAdmin);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(request.getName());
        assertThat(result.getBusinessNumber()).isEqualTo(request.getBusinessNumber());
        assertThat(result.getAdmin()).isEqualTo(mockAdmin);
    }

    @Test
    @DisplayName("브랜드 단건 조회 성공")
    void getBrand_success() {
        // given
        given(brandRepository.findById(1L)).willReturn(Optional.of(mockBrand));

        // when
        Brand result = brandService.getBrand(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(brandA);
    }

    @Test
    @DisplayName("브랜드 수정 성공")
    void updateBrand_success() {
        // given
        UpdateBrandRequest request = BrandFixture.createRequestForUpdateBrand("브랜드B");
        given(brandRepository.findByName(request.getName())).willReturn(Optional.empty());
        given(brandRepository.findById(1L)).willReturn(Optional.of(mockBrand));

        // when
        Brand updated = brandService.updateBrand(1L, request);

        // then
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("브랜드 삭제 성공")
    void deleteBrand_success() {
        // given
        given(brandRepository.findByIdAndAdminId(1L, 1L)).willReturn(Optional.of(mockBrand));

        // when
        Brand deleted = brandService.deleteBrand(1L, mockAdmin);

        // then
        assertThat(deleted).isNotNull();
        assertThat(deleted.getId()).isEqualTo(1L);
        assertThat(deleted.getAdmin()).isEqualTo(mockAdmin);
    }

}
