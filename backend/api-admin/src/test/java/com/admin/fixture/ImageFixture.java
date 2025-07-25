package com.admin.fixture;

import com.admin.mq.ImageProcessedMessage;
import com.admin.web.request.image.UploadUrlRequest;
import com.relation.product.Product;
import com.relation.productimage.ProductImage;
import org.springframework.test.util.ReflectionTestUtils;

public class ImageFixture {

    public static UploadUrlRequest createUploadUrlRequest(
        final Long productId,
        final String fileName,
        final String contentType,
        final boolean includeThumbnail
    ) {
        UploadUrlRequest request = new UploadUrlRequest();
        ReflectionTestUtils.setField(request, "productId", productId);
        ReflectionTestUtils.setField(request, "fileName", fileName);
        ReflectionTestUtils.setField(request, "contentType", contentType);
        ReflectionTestUtils.setField(request, "includeThumbnail", includeThumbnail);

        return request;
    }

    public static ProductImage createMockProductImage(Product product, String url, String fileName) {
        return ProductImage.builder()
            .product(product)
            .url(url)
            .fileName(fileName)
            .build();
    }

}
