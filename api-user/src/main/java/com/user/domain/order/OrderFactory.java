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
