package com.admin.mq;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductThumbnailMessage(
    @JsonProperty("size")
    String size,

    @JsonProperty("url")
    String url,

    @JsonProperty("key")
    String key
) {
}
