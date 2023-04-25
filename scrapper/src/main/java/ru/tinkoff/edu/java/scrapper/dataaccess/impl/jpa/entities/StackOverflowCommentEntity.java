package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.StackOverflowCommentPrimaryKey;

@Entity
@Data
@Table(name = "stack_overflow_comment")
public class StackOverflowCommentEntity {
    @EmbeddedId
    private StackOverflowCommentPrimaryKey primaryKey;
}
