package com.kafkahomework.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkahomework.entity.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "${spring.kafka.topic.name}")
    public void listen(List<Vehicle> recordBatch, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("------------------------------------------");
        recordBatch.forEach(record -> {
            try {
                log.info("First task consumer listened to: " + mapper.writeValueAsString(recordBatch));
            } catch (JsonProcessingException e) {
                log.error("Exception occurred during messages processing ", e);
            }
        });
        log.info("------------------------------------------");
    }
}
