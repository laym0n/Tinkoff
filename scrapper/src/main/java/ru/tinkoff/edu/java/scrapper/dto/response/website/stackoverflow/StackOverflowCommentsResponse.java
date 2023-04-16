package ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow;

import lombok.*;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter

public class StackOverflowCommentsResponse {
    private StackOverflowCommentResponse[] items;
}
