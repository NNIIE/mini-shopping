package com.admin.web.request.image;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UploadUrlRequest {

    private Long productId; // 상품 ID
    private String fileName; // 업로드할 파일명
    private String contentType; // 파일의 MIME 타입 (예: image/jpeg, image/png)

}
