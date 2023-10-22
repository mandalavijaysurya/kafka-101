package org.learning.kafka101Producer.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Book(
        @NotNull
        Integer bookId,
                   @NotBlank
                   String bookName,
                   @NotNull
                   String bookAuthor) {
}
