package ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow;

import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class StackOverflowUser {
    private String userName;
}
