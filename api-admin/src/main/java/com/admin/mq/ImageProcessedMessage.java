package com.admin.mq;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ImageProcessedMessage(
    @JsonProperty("productId")
    Long productId,

    @JsonProperty("originUrl")
    String originUrl,

    @JsonProperty("fileName")
    String originFileName,

    @JsonProperty("fileSize")
    Long originFileSize
) {
}
