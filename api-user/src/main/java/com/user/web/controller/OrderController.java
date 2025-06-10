package com.user.web.controller;

import com.storage.user.User;
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

    /**
     * 새로운 주문을 생성하고 결과를 반환합니다.
     *
     * @param request 생성할 주문의 정보가 담긴 요청 객체
     * @param user 현재 인증된 사용자 정보
     * @return 생성된 주문에 대한 응답 정보가 포함된 ResponseEntity
     */
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

