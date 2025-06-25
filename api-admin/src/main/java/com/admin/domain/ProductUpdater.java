package com.admin.domain;

import com.admin.web.request.product.UpdateProductRequest;
import com.relation.product.Product;

public class ProductUpdater {

    public static void apply(final Product product, final UpdateProductRequest request) {
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }
    }

}
