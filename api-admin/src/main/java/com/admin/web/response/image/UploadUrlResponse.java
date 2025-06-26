package com.admin.web.response.image;

public record UploadUrlResponse(
    String uploadUrl,     // 클라이언트가 사용할 업로드 URL
    String finalUrl,      // 업로드 완료 후 접근 가능한 최종 URL
    String s3Key
) {
}
