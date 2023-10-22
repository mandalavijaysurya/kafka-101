package org.learning.kafka101Producer.exception;

public class InvalidLibraryEventTypeException extends RuntimeException {
    private String message;
    public InvalidLibraryEventTypeException(String message) {
        super(message);
        this.message = message;
    }
}
