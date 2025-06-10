package com.admin.domain;

import com.admin.web.request.product.UpdateProductRequest;
import com.storage.product.Product;

public class ProductUpdater {

    /**
     * 주어진 요청 객체의 값이 null이 아닌 경우, 해당 값을 상품 객체에 반영하여 업데이트합니다.
     *
     * @param product 업데이트할 상품 객체
     * @param request 상품 정보 변경 요청 객체
     */
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

