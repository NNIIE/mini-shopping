package com.user.domain.order;

import com.relation.enums.OrderStatus;
import com.relation.order.Order;
import com.relation.order.OrderProduct;
import com.relation.product.Product;
import com.relation.user.User;

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
