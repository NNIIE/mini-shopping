package com.s3.service;

import com.support.response.PreSignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;


import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final S3Presigner s3Presigner;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.bucket.original}")
    private String originBucket;

    @Value("${aws.s3.bucket.thumbnail}")
    private String thumbnailBucket;

    private static final String PRODUCTS_FOLDER = "products/";

    public PreSignedUrlResponse generateUploadUrl(
        final Long productId,
        final String fileName,
        final String contentType
    ) {
        // 객체 키 생성 (기존 로직과 동일)
        final String objectKey = String.format("%s%s-%s-%s",
            PRODUCTS_FOLDER, productId, fileName, System.currentTimeMillis());

        // AWS SDK v2 방식의 PutObjectRequest 생성
        final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(originBucket)
            .key(objectKey)
            .contentType(contentType)
            .build();

        // Presigned URL 생성 (더 명확한 API)
        final PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))  // 10분 만료
            .putObjectRequest(putObjectRequest)
            .build();

        final String presignedUrl = s3Presigner.presignPutObject(presignRequest).url().toString();

        // 최종 URL 생성 (업로드 완료 후 접근할 URL)
        final String finalUrl = String.format(
            "https://%s.s3.%s.amazonaws.com/%s",
            originBucket,
            region,
            objectKey
        );

        return new PreSignedUrlResponse(presignedUrl, finalUrl, objectKey);
    }

}
