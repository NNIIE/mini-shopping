package com.admin.service;

import com.admin.exception.BusinessException;
import com.admin.exception.ErrorCode;
import com.storage.category.Category;
import com.storage.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 주어진 ID에 해당하는 카테고리를 조회합니다.
     *
     * @param id 조회할 카테고리의 ID
     * @return 조회된 Category 엔티티
     * @throws BusinessException 카테고리를 찾을 수 없는 경우 발생
     */
    public Category getCategoryById(final Long id) {
        return categoryRepository.findById(id).orElseThrow(
            () -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND)
        );
    }

}

