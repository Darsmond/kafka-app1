package com.kafkahomework;

import com.kafkahomework.entity.Vehicle;
import com.kafkahomework.kafka.KafkaConsumer;
import com.kafkahomework.kafka.KafkaProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.timeout;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class EmbeddedKafkaIntegrationTest {

    private final Vehicle event = new Vehicle("125", "20", "20");

    @SpyBean
    private KafkaConsumer consumer;

    @Autowired
    private KafkaProducer producer;

    @Captor
    private ArgumentCaptor<List<Vehicle>> argumentCaptor;

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;

    @Captor
    private ArgumentCaptor<String> topicArgumentCaptor;

    @Test
    public void kafkaSendingTest() {
        producer.send(event);
        Mockito.verify(consumer, timeout(1000).times(1)).listen(argumentCaptor.capture(),
                topicArgumentCaptor.capture());
        List<Vehicle> batchPayload = argumentCaptor.getValue();
        assertNotNull(batchPayload);
        Assertions.assertEquals(batchPayload.size(), 1);
        assertTrue(TOPIC_NAME.contains(topicArgumentCaptor.getValue()));
        testEvents(batchPayload);
    }

    private void testEvents(List<Vehicle> eventsPayload) {
        eventsPayload.forEach(record -> {
            assertNotNull(record);
            Assertions.assertEquals(event.getId(), record.getId());
            Assertions.assertEquals(event.getXCoordinate(), record.getXCoordinate());
            Assertions.assertEquals(event.getYCoordinate(), record.getYCoordinate());
        });
    }
}
