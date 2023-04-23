package ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow;

import lombok.*;


@ToString
@EqualsAndHashCode
@Data
public class StackOverflowAnswersResponse {
    private StackOverflowAnswerResponse[] items;
}
