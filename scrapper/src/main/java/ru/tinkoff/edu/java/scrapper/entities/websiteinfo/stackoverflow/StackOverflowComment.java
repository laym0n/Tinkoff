package ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow;

import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class StackOverflowComment {
    private int idComment;
}
