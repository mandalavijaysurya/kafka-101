package org.learning.kafka101Producer.domain;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record LibraryEvent(Integer libraryEventId,
                           LibraryEventType libraryEventType,
                           @NotNull @Valid
                           Book book) implements Serializable {


}
