package org.learning.kafka101Producer.exception;

import lombok.Getter;

@Getter
public class InvalidLibraryEventIdException extends RuntimeException {

    private String message;
    public InvalidLibraryEventIdException(String message) {
        super(message);
        this. message = message;
    }
}
