package ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class StackOverflowAnswersResponse {
    private StackOverflowAnswerResponse[] items;
}
