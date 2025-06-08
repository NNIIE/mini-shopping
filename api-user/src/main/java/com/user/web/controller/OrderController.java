package com.user.web.controller;

import com.storage.user.User;
import com.user.principal.CurrentUser;
import com.user.service.OrderService;
import com.user.web.request.order.CreateOrderRequest;
import com.user.web.response.order.OrderResponse;
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
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
        @RequestBody @Valid final CreateOrderRequest request,
        @CurrentUser final User user
    ) {
        final OrderResponse response = orderService.createOrder(request, user);
        return ResponseEntity.ok(response);
    }

}
