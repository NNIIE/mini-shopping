package com.admin.service;

import com.admin.mq.ImageProcessedMessage;
import com.admin.web.request.image.UploadUrlRequest;
import com.admin.web.response.image.PreSignedResponse;
import com.relation.product.Product;
import com.relation.productimage.ProductImage;
import com.relation.productimage.ProductImageRepository;
import com.s3.service.S3UploadService;
import com.support.dto.CreatePreSignedDto;
import com.support.response.PreSignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.bucket.original}")
    private String originBucket;

    @Value("${aws.s3.bucket.thumbnail}")
    private String thumbnailBucket;

    private static final String PRODUCTS_FOLDER = "products/";
    private static final String THUMBNAIL_SUFFIX = "_thumbnail";

    private final S3UploadService s3UploadService;
    private final ProductService productService;
    private final ProductImageRepository productImageRepository;

    public PreSignedResponse generateUploadUrl(final UploadUrlRequest request) {
        final Long productId = request.getProductId();
        final String contentType = request.getContentType();
        final String baseFileName = request.getFileName();

        final PreSignedUrlResponse originResponse = s3UploadService.generateUploadUrl(
            new CreatePreSignedDto(productId, baseFileName, contentType, PRODUCTS_FOLDER, region, originBucket)
        );

        if (!request.isIncludeThumbnail()) {
            return PreSignedResponse.originOnly(originResponse);
        }

        final PreSignedUrlResponse thumbnailResponse = s3UploadService.generateUploadUrl(
            new CreatePreSignedDto(productId, baseFileName + THUMBNAIL_SUFFIX, contentType, PRODUCTS_FOLDER, region, thumbnailBucket)
        );

        return PreSignedResponse.originAndThumbnail(originResponse, thumbnailResponse);
    }

    @Transactional
    public void saveImages(final ImageProcessedMessage message) {
        final Product product = productService.getProductById(message.productId());
        createProductImage(product, message);
    }

    private void createProductImage(final Product product, final ImageProcessedMessage message) {
        final ProductImage productImage = ProductImage.builder()
            .product(product)
            .url(message.originUrl())
            .fileName(message.originFileName())
            .fileSize(message.originFileSize())
            .build();

        productImageRepository.save(productImage);
    }

}
