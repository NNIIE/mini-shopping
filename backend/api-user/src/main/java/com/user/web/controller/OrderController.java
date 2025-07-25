package com.user.web.controller;

import com.relation.user.User;
import com.user.principal.CurrentUser;
import com.user.service.OrderService;
import com.user.web.request.order.CreateOrderRequest;
import com.user.web.response.order.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/order")
@Tag(name = "Order", description = "Order Management API")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "주문 생성")
    public ResponseEntity<OrderResponse> createOrder(
        @RequestBody @Valid final CreateOrderRequest request,
        @CurrentUser final User user
    ) {
        final OrderResponse response = orderService.createOrder(request, user);
        return ResponseEntity.ok(response);
    }

}
