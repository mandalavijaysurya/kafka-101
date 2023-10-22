package intg.org.learning.kafka101Producer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.learning.kafka101Producer.domain.LibraryEvent;
import org.learning.kafka101Producer.domain.LibraryEventType;
import org.learning.util.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = "library-events")
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"
        ,"spring.kafka.admin.properties.bootstrap-servers=${spring.embedded.kafka.brokers}"})
@Slf4j
class LibraryEventsControllerIntegrationTest {
    Consumer<Integer, String> consumer;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    EmbeddedKafkaBroker embeddedBroker;

    @Autowired
    ObjectMapper objectMapper;


    @BeforeEach
    void setUp(){
        var config = new HashMap<>(KafkaTestUtils.consumerProps("group-id", "true", embeddedBroker));
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        consumer = new DefaultKafkaConsumerFactory<Integer, String>(config).createConsumer();
        embeddedBroker.consumeFromAllEmbeddedTopics(consumer);
    }

    @AfterEach
    void tearDown(){
        consumer.close();
    }

    @Test
    void postEntityEvent() {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var recordSent = TestUtil.libraryEventRecord();
        var httpEntity = new HttpEntity<>(recordSent,httpHeaders);
        var responseEntity = restTemplate.exchange("/v1/library-event", HttpMethod.POST, httpEntity,LibraryEvent.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        ConsumerRecords<Integer, String> consumerRecords = KafkaTestUtils.getRecords(consumer);
        consumerRecords.forEach(record -> {
            var libraryEventActual = TestUtil.parseLibraryEventRecord(objectMapper,record.value());
            assertEquals(libraryEventActual, recordSent );
        });
    }

    @Test
    void putEventEntity(){
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var body = TestUtil.libraryEventRecordUpdate();
        var httpEntity = new HttpEntity<LibraryEvent>(body ,httpHeaders);
        var response = restTemplate.exchange("/v1/library-event",HttpMethod.PUT,httpEntity, LibraryEvent.class);
        log.info("Response Status Code: {}", response.getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ConsumerRecords<Integer, String> consumerRecords = KafkaTestUtils.getRecords(consumer);
        consumerRecords.forEach(record -> {
            var actualEvent = TestUtil.parseLibraryEventRecord(objectMapper,record.value());
            log.info("Actual: {}, Expected: {}", actualEvent, body);
            if(actualEvent.libraryEventType().equals(LibraryEventType.UPDATE))
                assertEquals(actualEvent,body);
        });
    }
}