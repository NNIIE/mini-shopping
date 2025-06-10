package com.admin.fixture;

import com.storage.category.Category;
import org.springframework.test.util.ReflectionTestUtils;

public class CategoryFixture {

    public static Category createCategoryWithId() {
        Category category = Category.builder()
            .name("테스트카테고리")
            .build();

        ReflectionTestUtils.setField(category, "id", 1L);

        return category;
    }

}

