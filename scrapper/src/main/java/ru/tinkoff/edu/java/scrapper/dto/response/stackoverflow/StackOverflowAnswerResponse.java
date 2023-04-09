package ru.tinkoff.edu.java.scrapper.dto.response.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record StackOverflowAnswerResponse(StackOverflowUserResponse owner,
        @JsonProperty("creation_date") OffsetDateTime creationDate,
        @JsonProperty("last_edit_date") OffsetDateTime lastEditDate,
        @JsonProperty("answer_id") int answerId) {
}
