package ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow;

import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;

import java.time.OffsetDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class StackOverflowAnswer {
    private int idAnswer;
    @HashCodeExclude
    private String userName;
    @HashCodeExclude
    private OffsetDateTime lastEditDate;

}
