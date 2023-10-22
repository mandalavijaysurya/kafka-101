package org.learning.kafka101Producer.exception.controllerAdvice;

import org.learning.kafka101Producer.exception.InvalidLibraryEventTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LibraryEventTypeExceptionHandler {

    @ExceptionHandler(InvalidLibraryEventTypeException.class)
    public ResponseEntity<String> eventTypeHandler(InvalidLibraryEventTypeException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
