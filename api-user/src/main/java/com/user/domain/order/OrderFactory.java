package com.user.domain.order;

import com.storage.enums.OrderStatus;
import com.storage.order.Order;
import com.storage.order.OrderProduct;
import com.storage.product.Product;
import com.storage.user.User;

public class OrderFactory {

    public static Order createOrder(final User user) {
        return Order.builder()
            .user(user)
            .status(OrderStatus.BEFORE_PAYMENT)
            .build();
    }

    /**
     * 지정된 주문, 상품, 수량으로 새로운 OrderProduct 객체를 생성합니다.
     *
     * @param order 주문 객체
     * @param product 상품 객체
     * @param quantity 주문 수량
     * @return 생성된 OrderProduct 인스턴스
     */
    public static OrderProduct createOrderProduct(
        final Order order,
        final Product product,
        final Integer quantity
    ) {
        return OrderProduct.builder()
            .order(order)
            .product(product)
            .quantity(quantity)
            .build();
    }

}

