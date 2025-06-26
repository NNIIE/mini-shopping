package com.admin.service;

import com.admin.mq.ImageProcessedMessage;
import com.admin.mq.ProductThumbnailMessage;
import com.admin.web.request.image.UploadUrlRequest;
import com.relation.product.Product;
import com.relation.productimage.ProductImage;
import com.relation.productimage.ProductImageRepository;
import com.relation.productthumbnail.ProductThumbnail;
import com.relation.productthumbnail.ProductThumbnailRepository;
import com.s3.service.S3UploadService;
import com.support.response.PreSignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3UploadService s3UploadService;
    private final ProductService productService;
    private final ProductImageRepository productImageRepository;
    private final ProductThumbnailRepository productThumbnailRepository;

    public PreSignedUrlResponse generateUploadUrl(final UploadUrlRequest request) {
        return s3UploadService.generateUploadUrl(
            request.getProductId(),
            request.getFileName(),
            request.getContentType()
        );
    }

    @Transactional
    public void saveImages(final ImageProcessedMessage message) {
        final Product product = productService.getProductById(message.productId());
        createProductImage(product, message);
        createProductThumbnail(product, message);
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

    private void createProductThumbnail(final Product product, final ImageProcessedMessage message) {
        for (ProductThumbnailMessage thumbnail : message.thumbnails()) {
            final ProductThumbnail productThumbnail = ProductThumbnail.builder()
                .product(product)
                .url(thumbnail.url())
                .sizeType(thumbnail.size())
                .build();

            productThumbnailRepository.save(productThumbnail);
        }
    }

}
