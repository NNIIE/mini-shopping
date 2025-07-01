package com.support.dto;

public record CreatePreSignedDto(
    Long productId,
    String fileName,
    String contentType,
    String folderName,
    String region,
    String bucket
) {
}
