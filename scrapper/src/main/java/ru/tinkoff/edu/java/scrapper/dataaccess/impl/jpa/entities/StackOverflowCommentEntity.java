package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.StackOverflowCommentPrimaryKey;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "stack_overflow_comment")
public class StackOverflowCommentEntity {
    @EmbeddedId
    private StackOverflowCommentPrimaryKey primaryKey;
    public StackOverflowCommentEntity(StackOverflowComment comment, int idWebsiteInfo){
        primaryKey = new StackOverflowCommentPrimaryKey(comment.getIdComment(), idWebsiteInfo);
    }
    public StackOverflowComment getStackOverflowComment(){
        return new StackOverflowComment(primaryKey.getId());
    }
}
