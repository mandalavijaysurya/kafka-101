package org.learning.kafka101.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.learning.kafka101.domain.LibraryEvent;
import org.learning.kafka101.producer.LibraryEventProducer;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LibraryEventsController {

    private LibraryEventProducer libraryEventProducer;

    public LibraryEventsController(LibraryEventProducer libraryEventProducer){
        this.libraryEventProducer = libraryEventProducer;
    }
    @PostMapping("/v1/library-event")
    public ResponseEntity<LibraryEvent> postEntityEvent(@RequestBody @Valid LibraryEvent libraryEvent)throws Throwable{
        log.info("libraryEvent: {}", libraryEvent);
        libraryEventProducer.sendLibraryEvent_approach2(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
