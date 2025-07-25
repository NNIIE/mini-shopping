package com.user.web.request.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class OrderItem {

    @NotNull
    private Long productId;

    @NotNull
    @Positive
    private Integer quantity;

}
