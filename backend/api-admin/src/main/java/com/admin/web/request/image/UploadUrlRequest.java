package com.admin.web.request.image;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UploadUrlRequest {

    private Long productId;
    private String fileName;
    private String contentType;
    private boolean includeThumbnail;

}
