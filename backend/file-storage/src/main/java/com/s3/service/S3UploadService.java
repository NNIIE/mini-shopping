package com.s3.service;

import com.support.dto.CreatePreSignedDto;
import com.support.response.PreSignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final S3Presigner s3Presigner;

    public PreSignedUrlResponse generateUploadUrl(final CreatePreSignedDto dto) {
        final String objectKey = String.format("%s%s-%s-%s",
            dto.folderName(), dto.productId(), dto.fileName(), System.currentTimeMillis());

        final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(dto.bucket())
            .key(objectKey)
            .contentType(dto.contentType())
            .build();

        final PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .putObjectRequest(putObjectRequest)
            .build();

        final String preSignedUrl = s3Presigner.presignPutObject(preSignRequest).url().toString();

        final String finalUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
            dto.bucket(), dto.region(), objectKey);

        return new PreSignedUrlResponse(preSignedUrl, finalUrl, objectKey);
    }

}
