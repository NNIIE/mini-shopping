package com.admin.service;

import com.admin.domain.ProductUpdater;
import com.admin.exception.BusinessException;
import com.admin.exception.ErrorCode;
import com.admin.web.request.product.CreateProductRequest;
import com.admin.web.request.product.UpdateProductRequest;
import com.storage.brand.Brand;
import com.storage.category.Category;
import com.storage.product.Product;
import com.storage.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandService brandService;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    public Page<Product> getProductsByBrand(final Long brandId, final Pageable pageable) {
        return productRepository.findByBrandId(brandId, pageable);
    }

    @Transactional(readOnly = true)
    public Product getProductById(final Long id) {
        return findProductById(id);
    }

    @Transactional
    public Product createProduct(final CreateProductRequest request) {
        final Brand brand = brandService.getBrand(request.getBrandId());
        final Category category = categoryService.getCategoryById(request.getCategoryId());
        final Product product = Product.builder()
            .brand(brand)
            .category(category)
            .name(request.getName())
            .price(request.getPrice())
            .stock(request.getStock())
            .build();

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(final Long id, final UpdateProductRequest request) {
        final Product product = findProductById(id);
        ProductUpdater.apply(product, request);

        return product;
    }

    @Transactional
    public Product deleteProduct(final Long id) {
        final Product product = findProductById(id);
        productRepository.delete(product);

        return product;
    }

    public Product findProductById(final Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

}

