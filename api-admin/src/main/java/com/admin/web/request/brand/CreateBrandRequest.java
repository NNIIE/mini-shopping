package com.admin.web.request.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.support.util.RegexpConstants.ENGLISH_KOREAN_REGEXP;

@Getter
@NoArgsConstructor
@ToString
public class CreateBrandRequest {

    @NotBlank(message = "브랜드 이름은 필수 입력 입니다.")
    @Pattern(regexp = ENGLISH_KOREAN_REGEXP, message = "브랜드 이름은 한글 또는 영어이어야 합니다.")
    private String name;

    @NotBlank(message = "사업자 번호는 필수 입력 입니다.")
    @Size(max = 10, message = "사업자 번호는 10자 이하이어야 합니다.")
    private String businessNumber;

}

