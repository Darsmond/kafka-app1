package com.kafkahomework.kafka;

import com.kafkahomework.entity.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, Vehicle> vehicleKafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String firstTaskTopic;

    public KafkaProducer(KafkaTemplate<String, Vehicle> vehicleKafkaTemplate){
        this.vehicleKafkaTemplate = vehicleKafkaTemplate;
    }

    public void send(Vehicle vehicle) {
        vehicleKafkaTemplate.send(firstTaskTopic, vehicle.getId(), vehicle);
        log.info("Vehicle " + vehicle.getId() + " was sent to topic first-task");
    }

}
