package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

@Entity
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stackoverflow_info")
@PrimaryKeyJoinColumn(name = "website_info_id")
public class StackOverflowInfoEntity extends WebsiteInfoEntity {
    @Column(name = "question_id")
    private int questionId;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "website_info_id")
    private Collection<StackOverflowAnswerEntity> answers;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "website_info_id")
    private Collection<StackOverflowCommentEntity> comments;

    public StackOverflowInfoEntity(StackOverflowInfo info) {
        super(info.getId());
        this.questionId = info.getLinkInfo().idQuestion();
        this.answers = info.getAnswers().values()
            .stream().map(i -> new StackOverflowAnswerEntity(i, info.getId())).toList();
        this.comments = info.getComments().values()
            .stream().map(i -> new StackOverflowCommentEntity(i, info.getId())).toList();
    }

    public StackOverflowInfoEntity(int questionId) {
        this(questionId, new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public StackOverflowInfo getWebsiteInfo() {
        Map<Integer, StackOverflowAnswer> answersForResult = answers.stream()
                .map(StackOverflowAnswerEntity::getStackOverflowAnswer)
                .collect(Collectors.toMap(StackOverflowAnswer::getIdAnswer, i -> i));
        Map<Integer, StackOverflowComment> commentForResult = comments.stream()
                .map(StackOverflowCommentEntity::getStackOverflowComment)
                .collect(Collectors.toMap(StackOverflowComment::getIdComment, i -> i));
        return new StackOverflowInfo(id,
                OffsetDateTime.of(lastCheckUpdate.toLocalDateTime(), ZoneOffset.MIN),
                new StackOverflowLinkInfo(questionId),
                commentForResult,
                answersForResult
        );
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        comments.forEach(i -> i.getPrimaryKey().setWebsiteInfoId(id));
        answers.forEach(i -> i.getPrimaryKey().setWebsiteInfoId(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        StackOverflowInfoEntity that = (StackOverflowInfoEntity) o;
        return getQuestionId() == that.getQuestionId()
                && new HashSet<>(getAnswers())
                    .equals(new HashSet<>(that.getAnswers()))
                && new HashSet<>(getComments())
                    .equals(new HashSet<>(that.getComments()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getQuestionId());
    }
}
