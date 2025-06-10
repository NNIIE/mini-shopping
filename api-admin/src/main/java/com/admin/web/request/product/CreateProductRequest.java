package com.admin.web.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

import static com.support.util.RegexpConstants.ENGLISH_KOREAN_REGEXP;

@Getter
@NoArgsConstructor
@ToString
public class CreateProductRequest {

    @NotNull
    private Long brandId;

    @NotNull
    private Long categoryId;

    @NotBlank(message = "상품 이름은 필수 입력 입니다.")
    @Pattern(regexp = ENGLISH_KOREAN_REGEXP, message = "상품 이름은 한글 또는 영어이어야 합니다.")
    private String name;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    @Positive
    private Integer stock;

}

