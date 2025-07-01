package com.admin.web.controller;

import com.admin.principal.CurrentAdmin;
import com.admin.service.ImageService;
import com.admin.web.request.image.UploadUrlRequest;
import com.admin.web.response.image.PreSignedResponse;
import com.relation.admin.Admin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/image")
@Tag(name = "Image", description = "Product Image Upload")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload-url")
    @Operation(summary = "이미지 업로드 URL 발급")
    public ResponseEntity<PreSignedResponse> generateUploadUrl(
        @RequestBody @Valid final UploadUrlRequest request
//        @CurrentAdmin final Admin admin
    ) {
        final PreSignedResponse response = imageService.generateUploadUrl(request);
        return ResponseEntity.ok(response);
    }

}
