package com.admin.fixture;

import com.admin.web.request.product.CreateProductRequest;
import com.admin.web.request.product.UpdateProductRequest;
import com.relation.brand.Brand;
import com.relation.category.Category;
import com.relation.product.Product;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product createProductWithId(Brand brand, Category category) {
        Product product = Product.builder()
            .brand(brand)
            .category(category)
            .name("테스트상품")
            .price(BigDecimal.valueOf(10000))
            .stock(10)
            .build();
        ReflectionTestUtils.setField(product, "id", 1L);

        return product;
    }

    public static CreateProductRequest createRequestForCreateProduct(
        Long brandId,
        Long categoryId
    ) {
        CreateProductRequest request = new CreateProductRequest();
        ReflectionTestUtils.setField(request, "brandId", brandId);
        ReflectionTestUtils.setField(request, "categoryId", categoryId);
        ReflectionTestUtils.setField(request, "name", "테스트상품");
        ReflectionTestUtils.setField(request, "price", BigDecimal.valueOf(10000));
        ReflectionTestUtils.setField(request, "stock", 10);

        return request;
    }

    public static UpdateProductRequest createRequestForUpdateProduct(
        String name,
        BigDecimal price,
        Integer stock
    ) {
        UpdateProductRequest request = new UpdateProductRequest();
        ReflectionTestUtils.setField(request, "name", name);
        ReflectionTestUtils.setField(request, "price", price);
        ReflectionTestUtils.setField(request, "stock", stock);

        return request;
    }

}
