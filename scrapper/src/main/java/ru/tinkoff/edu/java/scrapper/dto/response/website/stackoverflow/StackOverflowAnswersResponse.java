package ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow;

import lombok.*;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class StackOverflowAnswersResponse {
    private StackOverflowAnswerResponse[] items;
}
