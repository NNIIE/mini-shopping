package com.admin.web.response.image;

public record SaveImageResponse(
    Long imageId,        // 저장된 이미지의 DB ID
    String imageUrl,     // 저장된 이미지의 접근 URL
    String message       // 저장 결과 메시지
) {
}
