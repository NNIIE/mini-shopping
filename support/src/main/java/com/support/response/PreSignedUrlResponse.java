package com.support.response;

public record PreSignedUrlResponse(
    String uploadUrl,
    String finalUrl,
    String s3Key
) {
}
