package unit.org.learning.kafka101Producer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.learning.kafka101Producer.controller.LibraryEventsController;
import org.learning.kafka101Producer.domain.LibraryEvent;
import org.learning.kafka101Producer.producer.LibraryEventProducer;
import org.learning.util.TestUtil;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LibraryEventsController.class)
class LibraryEventsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LibraryEventProducer libraryEventProducer;

    @BeforeEach
    void setUp(){

    }

    @Test
    void postEntityEvent() throws Exception {
        var json = TestUtil.libraryEventRecord();
        var temp = when(libraryEventProducer.sendLibraryEvent_approach3(isA(LibraryEvent.class)))
                .thenReturn(Mockito.any());

        MockHttpServletRequestBuilder requestBuilder = post("/v1/library-event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(json));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());
        assertNotNull(temp);
    }

    @Test
    void postEntityEvent_InvalidValues() throws Exception {
        var json = objectMapper.writeValueAsString(TestUtil.libraryEventRecordWithInvalidBook());
        when(libraryEventProducer.sendLibraryEvent_approach3(isA(LibraryEvent.class)))
                .thenReturn(null);
        mockMvc.perform(post("/v1/library-event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestUtil.libraryEventRecordWithInvalidBook())))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void putEntityEvent_InvalidValues() throws Exception{
        var json = objectMapper.writeValueAsString(TestUtil.libraryEventRecordUpdate());
        when(libraryEventProducer.sendLibraryEvent(isA(LibraryEvent.class)))
                .thenReturn(null);
        mockMvc.perform(put("/v1/library-event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestUtil.libraryEventRecordUpdate())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(""));

    }

}