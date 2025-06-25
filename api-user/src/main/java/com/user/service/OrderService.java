package com.user.service;

import com.relation.order.Order;
import com.relation.order.OrderProduct;
import com.relation.order.OrderProductRepository;
import com.relation.order.OrderRepository;
import com.relation.product.Product;
import com.relation.product.ProductRepository;
import com.relation.user.User;
import com.user.domain.order.OrderFactory;
import com.user.exception.BusinessException;
import com.user.exception.ErrorCode;
import com.user.web.request.order.CreateOrderRequest;
import com.user.web.response.order.OrderProductResponse;
import com.user.web.response.order.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponse createOrder(final CreateOrderRequest request, final User user) {
        final Order order = OrderFactory.createOrder(user);
        orderRepository.save(order);

        final List<OrderProductResponse> orderProductResponses = request.getOrderItems().stream()
                .map(orderItem -> {
                    final Product product = productRepository.findById(orderItem.getProductId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

                    if (product.getStock() < orderItem.getQuantity()) {
                        throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
                    }

                    product.setStock(product.getStock() - orderItem.getQuantity());
                    productRepository.save(product);

                    final OrderProduct orderProduct = OrderFactory.createOrderProduct(order, product, orderItem.getQuantity());
                    orderProductRepository.save(orderProduct);

                    return new OrderProductResponse(
                        orderProduct.getId(),
                        product.getId(),
                        product.getPrice(),
                        orderItem.getQuantity()
                    );
                }).toList();

        final BigDecimal totalPrice = calculateTotalPrice(orderProductResponses);
        return new OrderResponse(order.getId(), orderProductResponses, totalPrice);
    }

    private static BigDecimal calculateTotalPrice(final List<OrderProductResponse> orderProductResponses) {
        return orderProductResponses.stream()
            .map(resp -> resp.price().multiply(BigDecimal.valueOf(resp.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
