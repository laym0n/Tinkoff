package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.StackOverflowAnswerPrimaryKey;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stack_overflow_answer")
public class StackOverflowAnswerEntity {
    @EmbeddedId
    private StackOverflowAnswerPrimaryKey primaryKey;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "last_edit_date_time")
    private Timestamp lastEditDateTime;
    public StackOverflowAnswerEntity(StackOverflowAnswer answer, int idWebsiteInfo){
        primaryKey = new StackOverflowAnswerPrimaryKey(answer.getIdAnswer(), idWebsiteInfo);
        lastEditDateTime = Timestamp.valueOf(answer.getLastEditDate().toLocalDateTime());
        userName = answer.getUserName();
    }
    public StackOverflowAnswer getStackOverflowAnswer(){
        return new StackOverflowAnswer(primaryKey.getId(), userName,
                OffsetDateTime.of(lastEditDateTime.toLocalDateTime(), ZoneOffset.MIN));
    }
}
