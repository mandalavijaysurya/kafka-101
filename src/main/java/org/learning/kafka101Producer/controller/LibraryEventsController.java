package org.learning.kafka101Producer.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.learning.kafka101Producer.domain.LibraryEvent;
import org.learning.kafka101Producer.domain.LibraryEventType;
import org.learning.kafka101Producer.exception.InvalidLibraryEventIdException;
import org.learning.kafka101Producer.exception.InvalidLibraryEventTypeException;
import org.learning.kafka101Producer.producer.LibraryEventProducer;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LibraryEventsController {

    private final LibraryEventProducer libraryEventProducer;

    public LibraryEventsController(LibraryEventProducer libraryEventProducer){
        this.libraryEventProducer = libraryEventProducer;
    }
    @PostMapping("/v1/library-event")
    public ResponseEntity<LibraryEvent> postEntityEvent(@RequestBody @Valid LibraryEvent libraryEvent)throws Throwable{
        log.info("libraryEvent: {}", libraryEvent);
        libraryEventProducer.sendLibraryEvent_approach2(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
    @PutMapping("/v1/library-event")
    public ResponseEntity<String> putEntityEvent(@RequestBody @Valid LibraryEvent libraryEvent)throws Throwable{
        log.info("libraryEvent: {}", libraryEvent);
        if(libraryEvent.libraryEventId() == null)
            throw new InvalidLibraryEventIdException("Library event is null, so you can't update book");
        if(!libraryEvent.libraryEventType().equals(LibraryEventType.UPDATE))
            throw new InvalidLibraryEventTypeException("LibraryEventType is not UPDATE, so you can't update book");
        libraryEventProducer.sendLibraryEvent_approach2(libraryEvent);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
