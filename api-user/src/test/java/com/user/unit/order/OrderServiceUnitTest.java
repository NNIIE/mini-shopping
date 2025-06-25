package com.user.unit.order;

import com.relation.order.Order;
import com.relation.order.OrderProduct;
import com.relation.order.OrderProductRepository;
import com.relation.order.OrderRepository;
import com.relation.product.Product;
import com.relation.product.ProductRepository;
import com.relation.user.User;
import com.user.fixture.UserFixture;
import com.user.service.OrderService;
import com.user.web.request.order.CreateOrderRequest;
import com.user.web.request.order.OrderItem;
import com.user.web.response.order.OrderProductResponse;
import com.user.web.response.order.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private User mockUser;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        mockUser = UserFixture.createUser(UserFixture.createUserAccount());
        mockProduct = Product.builder()
            .name("테스트상품")
            .price(BigDecimal.valueOf(10000))
            .stock(10)
            .build();

        given(productRepository.findById(mockProduct.getId())).willReturn(Optional.of(mockProduct));
        given(productRepository.save(any(Product.class))).willReturn(mockProduct);

        given(orderRepository.save(any(Order.class))).willAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            ReflectionTestUtils.setField(order, "id", 1L);
            return order;
        });

        given(orderProductRepository.save(any(OrderProduct.class))).willAnswer(invocation -> {
            OrderProduct op = invocation.getArgument(0);
            ReflectionTestUtils.setField(op, "id", 1L);
            return op;
        });
    }

    @Test
    @DisplayName("주문 생성 성공")
    void createOrder_success() {
        // given
        int orderQuantity = 2;
        OrderItem orderItem = new OrderItem();
        ReflectionTestUtils.setField(orderItem, "productId", mockProduct.getId());
        ReflectionTestUtils.setField(orderItem, "quantity", orderQuantity);

        CreateOrderRequest request = new CreateOrderRequest();
        ReflectionTestUtils.setField(request, "orderItems", List.of(orderItem));

        // when
        OrderResponse result = orderService.createOrder(request, mockUser);
        BigDecimal expectedTotalPrice = mockProduct.getPrice().multiply(BigDecimal.valueOf(orderQuantity));
        OrderProductResponse productResponse = result.orderProducts().get(0);

        // then
        assertAll(
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result.orderId()).isEqualTo(1L),
            () -> assertThat(result.orderProducts()).hasSize(1),
            () -> assertThat(productResponse.productId()).isEqualTo(mockProduct.getId()),
            () -> assertThat(productResponse.quantity()).isEqualTo(orderQuantity),
            () -> assertThat(productResponse.price()).isEqualTo(mockProduct.getPrice()),
            () -> assertThat(result.totalPrice()).isEqualTo(expectedTotalPrice),
            () -> assertThat(mockProduct.getStock()).isEqualTo(10 - orderQuantity)
        );

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productRepository, times(1)).findById(mockProduct.getId());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(orderProductRepository, times(1)).save(any(OrderProduct.class));
    }


}
