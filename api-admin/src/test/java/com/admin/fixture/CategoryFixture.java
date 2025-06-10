package com.admin.fixture;

import com.storage.category.Category;
import org.springframework.test.util.ReflectionTestUtils;

public class CategoryFixture {

    /**
     * ID가 1로 설정된 테스트용 Category 객체를 생성하여 반환합니다.
     *
     * @return ID가 1이고 이름이 "테스트카테고리"인 Category 객체
     */
    public static Category createCategoryWithId() {
        Category category = Category.builder()
            .name("테스트카테고리")
            .build();

        ReflectionTestUtils.setField(category, "id", 1L);

        return category;
    }

}

