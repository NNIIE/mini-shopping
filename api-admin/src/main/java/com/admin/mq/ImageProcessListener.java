package com.admin.mq;

import com.admin.service.ImageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageProcessListener {

    private final ImageService imageService;
    private final ObjectMapper objectMapper;

    @SqsListener("${aws.sqs.queue.image-processed}")
    public void handleImageProcessed(final String message) throws JsonProcessingException {
        final ImageProcessedMessage processedMessage = objectMapper.readValue(message, ImageProcessedMessage.class);
        imageService.saveImages(processedMessage);
    }

}
