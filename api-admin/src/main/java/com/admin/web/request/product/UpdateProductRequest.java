package com.admin.web.request.product;

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
public class UpdateProductRequest {

    @Pattern(regexp = ENGLISH_KOREAN_REGEXP, message = "상품 이름은 한글 또는 영어이어야 합니다.")
    private String name;

    @Positive
    private BigDecimal price;

    @Positive
    private Integer stock;

    public boolean hasName() {
        return name != null;
    }

    public boolean hasPrice() {
        return price != null;
    }

    /**
     * 재고(stock) 필드가 설정되어 있는지 여부를 반환합니다.
     *
     * @return 재고 값이 null이 아니면 true, 그렇지 않으면 false
     */
    public boolean hasStock() {
        return stock != null;
    }

}

