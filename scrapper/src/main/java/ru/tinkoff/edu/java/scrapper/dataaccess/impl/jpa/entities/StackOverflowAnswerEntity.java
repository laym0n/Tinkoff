package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.StackOverflowAnswerPrimaryKey;

import java.time.OffsetDateTime;

@Entity
@Data
@Table(name = "stack_overflow_answer")
public class StackOverflowAnswerEntity {
    @EmbeddedId
    private StackOverflowAnswerPrimaryKey primaryKey;
    @Column(name = "last_edit_date_time")
    private OffsetDateTime lastEditDateTime;
}
