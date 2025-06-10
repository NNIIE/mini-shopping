package com.user.web.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class CreateOrderRequest {

    @NotNull
    private List<OrderItem> orderItems;

}

