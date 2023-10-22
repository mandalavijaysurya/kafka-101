package org.learning.kafka101Producer.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learning.kafka101Producer.domain.Book;
import org.learning.kafka101Producer.domain.LibraryEvent;
import org.learning.kafka101Producer.domain.LibraryEventType;
import org.learning.kafka101Producer.producer.LibraryEventProducer;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {LibraryEventsController.class})
@ExtendWith(SpringExtension.class)
class LibraryEventsControllerDiffblueTest {
    @MockBean
    private LibraryEventProducer libraryEventProducer;

    @Autowired
    private LibraryEventsController libraryEventsController;

    /**
     * Method under test: {@link LibraryEventsController#putEntityEvent(LibraryEvent)}
     */
    @Test
    void testPutEntityEvent() throws Exception {
        ProducerRecord<Integer, String> producerRecord = new ProducerRecord<>("Topic", "42");

        when(libraryEventProducer.sendLibraryEvent_approach2(Mockito.<LibraryEvent>any())).thenReturn(
                new SendResult<>(producerRecord, new RecordMetadata(new TopicPartition("Topic", 1), 1L, 1, 10L, 3, 3)));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/v1/library-event")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper
                .writeValueAsString(new LibraryEvent(1, LibraryEventType.UPDATE, new Book(1, "Book Name", "JaneDoe"))));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(libraryEventsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    /**
     * Method under test: {@link LibraryEventsController#putEntityEvent(LibraryEvent)}
     */
    @Test
    void testPutEntityEvent2() throws Exception {
        ProducerRecord<Integer, String> producerRecord = new ProducerRecord<>("Topic", "42");

        when(libraryEventProducer.sendLibraryEvent_approach2(Mockito.<LibraryEvent>any())).thenReturn(
                new SendResult<>(producerRecord, new RecordMetadata(new TopicPartition("Topic", 1), 1L, 1, 10L, 3, 3)));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/v1/library-event")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper
                .writeValueAsString(new LibraryEvent(1, LibraryEventType.UPDATE, new Book(null, "Book Name", "JaneDoe"))));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(libraryEventsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}

