package ru.tinkoff.edu.java.scrapper.dto.response.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record StackOverflowCommentResponse(StackOverflowUserResponse owner,
                                           @JsonProperty("creation_date") OffsetDateTime createdAt,
                                           @JsonProperty("comment_id") int idComment) {
}
