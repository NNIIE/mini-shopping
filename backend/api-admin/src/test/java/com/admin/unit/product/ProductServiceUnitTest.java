package com.admin.unit.product;

import com.admin.fixture.AdminFixture;
import com.admin.fixture.BrandFixture;
import com.admin.fixture.CategoryFixture;
import com.admin.fixture.ProductFixture;
import com.admin.service.BrandService;
import com.admin.service.CategoryService;
import com.admin.service.ProductService;
import com.admin.web.request.product.CreateProductRequest;
import com.admin.web.request.product.UpdateProductRequest;
import com.relation.brand.Brand;
import com.relation.category.Category;
import com.relation.product.Product;
import com.relation.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandService brandService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private Brand mockBrand;
    private Category mockCategory;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        mockBrand = BrandFixture.createBrand(AdminFixture.createAdminWithAnId(), "테스트브랜드", "123123");
        mockCategory = CategoryFixture.createCategoryWithId();
        mockProduct = ProductFixture.createProductWithId(mockBrand, mockCategory);
    }

    @Test
    @DisplayName("브랜드별 상품 목록 조회 성공")
    void getProductsByBrand_success() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(mockProduct));
        given(productRepository.findByBrandId(eq(mockBrand.getId()), any())).willReturn(page);

        // when
        Page<Product> result = productService.getProductsByBrand(mockBrand.getId(), pageRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).containsExactly(mockProduct);
    }

    @Test
    @DisplayName("상품 단건 조회 성공")
    void getProductById_success() {
        // given
        given(productRepository.findById(1L)).willReturn(Optional.of(mockProduct));

        // when
        Product result = productService.getProductById(1L);

        // then
        assertThat(result).isEqualTo(mockProduct);
    }

    @Test
    @DisplayName("상품 생성 성공")
    void createProduct_success() {
        // given
        CreateProductRequest request = ProductFixture.createRequestForCreateProduct(mockBrand.getId(), mockCategory.getId());
        given(brandService.getBrand(request.getBrandId())).willReturn(mockBrand);
        given(categoryService.getCategoryById(request.getCategoryId())).willReturn(mockCategory);
        given(productRepository.save(any(Product.class))).willReturn(mockProduct);

        // when
        Product result = productService.createProduct(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBrand()).isEqualTo(mockBrand);
        assertThat(result.getCategory()).isEqualTo(mockCategory);
        assertThat(result.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("상품 수정 성공")
    void updateProduct_success() {
        // given
        UpdateProductRequest request = ProductFixture.createRequestForUpdateProduct("변경상품", BigDecimal.valueOf(12345), 77);
        given(productRepository.findById(1L)).willReturn(Optional.of(mockProduct));

        // when
        Product updated = productService.updateProduct(1L, request);

        // then
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo(request.getName());
        assertThat(updated.getPrice()).isEqualTo(request.getPrice());
        assertThat(updated.getStock()).isEqualTo(request.getStock());
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void deleteProduct_success() {
        // given
        given(productRepository.findById(1L)).willReturn(Optional.of(mockProduct));

        // when
        Product deleted = productService.deleteProduct(1L);

        // then
        assertThat(deleted).isNotNull();
        assertThat(deleted.getId()).isEqualTo(1L);
    }


}
