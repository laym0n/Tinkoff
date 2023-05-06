package ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow;

import lombok.*;


@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class StackOverflowAnswersResponse {
    private StackOverflowAnswerResponse[] items;
}
