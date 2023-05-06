package ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeExclude;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class StackOverflowAnswer {
    private int idAnswer;
    private String userName;
    private OffsetDateTime lastEditDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StackOverflowAnswer answer = (StackOverflowAnswer) o;
        return getIdAnswer() == answer.getIdAnswer() && getUserName().equals(answer.getUserName()) &&
                lastEditDate.toLocalDateTime().truncatedTo(ChronoUnit.SECONDS)
                        .equals(answer.getLastEditDate().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdAnswer());
    }
}
