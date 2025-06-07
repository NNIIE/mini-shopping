package com.admin.web.request.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.support.util.RegexpConstants.ENGLISH_KOREAN_REGEXP;

@Getter
@NoArgsConstructor
@ToString
public class UpdateBrandRequest {

    @NotBlank(message = "브랜드 이름은 필수 입력 입니다.")
    @Pattern(regexp = ENGLISH_KOREAN_REGEXP, message = "브랜드 이름은 한글 또는 영어이어야 합니다.")
    private String name;

}
