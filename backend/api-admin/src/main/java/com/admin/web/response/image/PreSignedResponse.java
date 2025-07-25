package com.admin.web.response.image;

import com.support.response.PreSignedUrlResponse;
import lombok.Getter;

@Getter
public class PreSignedResponse {

    private final PreSignedUrlResponse originPreSigned;
    private final PreSignedUrlResponse thumbnailPreSigned;

    private PreSignedResponse(
        final PreSignedUrlResponse originPreSigned,
        final PreSignedUrlResponse thumbnailPreSigned
    ) {
        this.originPreSigned = originPreSigned;
        this.thumbnailPreSigned = thumbnailPreSigned;
    }

    public static PreSignedResponse originOnly(
        final PreSignedUrlResponse originPreSigned
    ) {
        return new PreSignedResponse(originPreSigned, null);
    }

    public static PreSignedResponse originAndThumbnail(
        final PreSignedUrlResponse originPreSigned,
        final PreSignedUrlResponse thumbnailPreSigned
    ) {
        return new PreSignedResponse(originPreSigned, thumbnailPreSigned);
    }

}
