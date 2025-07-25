package com.admin.unit.image;

import com.admin.fixture.*;
import com.admin.mq.ImageProcessedMessage;
import com.admin.service.ImageService;
import com.admin.service.ProductService;
import com.admin.web.request.image.UploadUrlRequest;
import com.admin.web.response.image.PreSignedResponse;
import com.relation.brand.Brand;
import com.relation.category.Category;
import com.relation.product.Product;
import com.relation.productimage.ProductImage;
import com.relation.productimage.ProductImageRepository;
import com.s3.service.S3UploadService;
import com.support.dto.CreatePreSignedDto;
import com.support.response.PreSignedUrlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class ImageServiceUnitTest {

    @Mock
    private S3UploadService s3UploadService;

    @Mock
    private ProductService productService;

    @Mock
    private ProductImageRepository productImageRepository;

    @InjectMocks
    private ImageService imageService;

    private Product mockProduct;

    private static final String TEST_REGION = "ap-northeast-2";
    private static final String TEST_ORIGIN_BUCKET = "mini-shopping-images-original";
    private static final String TEST_THUMBNAIL_BUCKET = "mini-shopping-images-thumbnail";

    @BeforeEach
    void setUp() {
        Brand mockBrand = BrandFixture.createBrand(AdminFixture.createAdminWithAnId(), "테스트브랜드", "123123");
        Category mockCategory = CategoryFixture.createCategoryWithId();
        mockProduct = ProductFixture.createProductWithId(mockBrand, mockCategory);

        ReflectionTestUtils.setField(imageService, "region", TEST_REGION);
        ReflectionTestUtils.setField(imageService, "originBucket", TEST_ORIGIN_BUCKET);
        ReflectionTestUtils.setField(imageService, "thumbnailBucket", TEST_THUMBNAIL_BUCKET);
    }

    @Test
    @DisplayName("원본 이미지만 업로드")
    void originImageUploadTest() {
        // Given
        final UploadUrlRequest request = ImageFixture.createUploadUrlRequest(
            123L, "sample-photo.jpg", "image/jpeg", false
        );

        final PreSignedUrlResponse mockOriginResponse = new PreSignedUrlResponse(
            "https://presigned-origin-url.com/upload",
            "https://final-origin-url.com/file",
            "products/123-sample-photo.jpg-1672531200000"
        );

        when(s3UploadService.generateUploadUrl(any(CreatePreSignedDto.class)))
            .thenReturn(mockOriginResponse);

        final PreSignedResponse response = imageService.generateUploadUrl(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getOriginPreSigned()).isEqualTo(mockOriginResponse);
        assertThat(response.getThumbnailPreSigned()).isNull(); // 썸네일은 null이어야 함
        verify(s3UploadService, times(1)).generateUploadUrl(any(CreatePreSignedDto.class));

        final ArgumentCaptor<CreatePreSignedDto> dtoCaptor = ArgumentCaptor.forClass(CreatePreSignedDto.class);
        verify(s3UploadService).generateUploadUrl(dtoCaptor.capture());

        final CreatePreSignedDto capturedDto = dtoCaptor.getValue();
        assertThat(capturedDto.productId()).isEqualTo(123L);
        assertThat(capturedDto.fileName()).isEqualTo("sample-photo.jpg");
        assertThat(capturedDto.contentType()).isEqualTo("image/jpeg");
        assertThat(capturedDto.bucket()).isEqualTo(TEST_ORIGIN_BUCKET);
    }

    @Test
    @DisplayName("원본 + 썸네일 업로드")
    void originAndThumbnailImageTest() {
        // Given
        final UploadUrlRequest request = ImageFixture.createUploadUrlRequest(
            456L, "product-image.png", "image/png", true
        );

        final PreSignedUrlResponse mockOriginResponse = new PreSignedUrlResponse(
            "https://presigned-origin-url.com/upload",
            "https://final-origin-url.com/file",
            "products/456-product-image.png-1672531200000"
        );

        final PreSignedUrlResponse mockThumbnailResponse = new PreSignedUrlResponse(
            "https://presigned-thumbnail-url.com/upload",
            "https://final-thumbnail-url.com/file",
            "products/456-product-image.png_thumbnail-1672531200000"
        );

        when(s3UploadService.generateUploadUrl(any(CreatePreSignedDto.class)))
            .thenReturn(mockOriginResponse)
            .thenReturn(mockThumbnailResponse);

        // When
        final PreSignedResponse response = imageService.generateUploadUrl(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getOriginPreSigned()).isEqualTo(mockOriginResponse);
        assertThat(response.getThumbnailPreSigned()).isEqualTo(mockThumbnailResponse);
        verify(s3UploadService, times(2)).generateUploadUrl(any(CreatePreSignedDto.class));

        final ArgumentCaptor<CreatePreSignedDto> dtoCaptor = ArgumentCaptor.forClass(CreatePreSignedDto.class);
        verify(s3UploadService, times(2)).generateUploadUrl(dtoCaptor.capture());

        final var capturedDtos = dtoCaptor.getAllValues();
        final CreatePreSignedDto originDto = capturedDtos.get(0);
        assertThat(originDto.productId()).isEqualTo(456L);
        assertThat(originDto.fileName()).isEqualTo("product-image.png");
        assertThat(originDto.bucket()).isEqualTo(TEST_ORIGIN_BUCKET);

        final CreatePreSignedDto thumbnailDto = capturedDtos.get(1);
        assertThat(thumbnailDto.productId()).isEqualTo(456L);
        assertThat(thumbnailDto.fileName()).isEqualTo("product-image.png_thumbnail");
        assertThat(thumbnailDto.bucket()).isEqualTo(TEST_THUMBNAIL_BUCKET);
    }

    @Test
    @DisplayName("SQS 메시지 처리 및 DB 저장")
    void saveImages_WithValidMessage_ShouldSaveProductImageToDatabase() {
        // Given
        final ImageProcessedMessage message = new ImageProcessedMessage(
            789L,
            "https://s3.amazonaws.com/original.jpg",
            "uploaded-file.jpg",
            1048576L
        );

        when(productService.getProductById(789L)).thenReturn(mockProduct);
        final ProductImage savedImage = ImageFixture.createMockProductImage(mockProduct, message.originUrl(), message.originFileName());
        when(productService.getProductById(789L)).thenReturn(mockProduct);
        when(productImageRepository.save(any(ProductImage.class))).thenReturn(savedImage);

        // When
        imageService.saveImages(message);

        // Then
        verify(productService, times(1)).getProductById(789L);
        verify(productImageRepository, times(1)).save(any(ProductImage.class));

        final ArgumentCaptor<ProductImage> imageCaptor = ArgumentCaptor.forClass(ProductImage.class);
        verify(productImageRepository).save(imageCaptor.capture());

        final ProductImage capturedImage = imageCaptor.getValue();
        assertThat(capturedImage.getProduct()).isEqualTo(mockProduct);
        assertThat(capturedImage.getUrl()).isEqualTo("https://s3.amazonaws.com/original.jpg");
        assertThat(capturedImage.getFileName()).isEqualTo("uploaded-file.jpg");
        assertThat(capturedImage.getFileSize()).isEqualTo(1048576L);
    }

}
