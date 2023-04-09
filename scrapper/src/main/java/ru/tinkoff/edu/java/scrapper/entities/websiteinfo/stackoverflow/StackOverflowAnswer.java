package ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StackOverflowAnswer {
    private StackOverflowUser stackOverflowUser;
    private int idAnswer;
    private OffsetDateTime lastEditDate;
}
