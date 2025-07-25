package com.admin.service;

import com.admin.exception.BusinessException;
import com.admin.exception.ErrorCode;
import com.relation.category.Category;
import com.relation.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category getCategoryById(final Long id) {
        return categoryRepository.findById(id).orElseThrow(
            () -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND)
        );
    }

}
