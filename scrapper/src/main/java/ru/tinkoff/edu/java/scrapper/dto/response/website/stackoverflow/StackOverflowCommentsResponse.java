package ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow;

import lombok.*;

@Data
@ToString
@EqualsAndHashCode

public class StackOverflowCommentsResponse {
    private StackOverflowCommentResponse[] items;
}
