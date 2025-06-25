package com.admin.fixture;

import com.admin.web.request.brand.CreateBrandRequest;
import com.admin.web.request.brand.UpdateBrandRequest;
import com.relation.admin.Admin;
import com.relation.brand.Brand;
import org.springframework.test.util.ReflectionTestUtils;

public class BrandFixture {

    public static Brand createBrand(
        Admin admin,
        String name,
        String businessNumber
    ) {
        Brand brand = Brand.builder()
            .admin(admin)
            .name(name)
            .businessNumber(businessNumber)
            .build();

        ReflectionTestUtils.setField(brand, "id", 1L);
        return brand;
    }

    public static CreateBrandRequest createRequestForCreateBrand(
        String name,
        String businessNumber
    ) {
        CreateBrandRequest request = new CreateBrandRequest();
        ReflectionTestUtils.setField(request, "name", name);
        ReflectionTestUtils.setField(request, "businessNumber", businessNumber);

        return request;
    }

    public static UpdateBrandRequest createRequestForUpdateBrand(String name) {
        UpdateBrandRequest request = new UpdateBrandRequest();
        ReflectionTestUtils.setField(request, "name", name);

        return request;
    }


}
