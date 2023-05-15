package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.ToString;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

@Data
@ToString(callSuper = true)
public final class StackOverflowInfo extends WebsiteInfo {
    private StackOverflowLinkInfo linkInfo;
    private Map<Integer, StackOverflowComment> comments;
    private Map<Integer, StackOverflowAnswer> answers;

    public StackOverflowInfo(
        int id,
        OffsetDateTime lastCheckUpdateDateTime,
        StackOverflowLinkInfo linkInfo,
        Map<Integer, StackOverflowComment> comments,
        Map<Integer, StackOverflowAnswer> answers
    ) {
        super(id, lastCheckUpdateDateTime);
        this.linkInfo = linkInfo;
        this.comments = comments;
        this.answers = answers;
    }

    public StackOverflowInfo(StackOverflowLinkInfo linkInfo) {
        this(0, OffsetDateTime.now(), linkInfo, new HashMap<>(), new HashMap<>());
    }

    public StackOverflowInfo(int id, OffsetDateTime lastCheckUpdateDateTime, StackOverflowLinkInfo linkInfo) {
        this(id, lastCheckUpdateDateTime, linkInfo, new HashMap<>(), new HashMap<>());
    }

    public StackOverflowInfo(int id, StackOverflowLinkInfo linkInfo) {
        this(id, OffsetDateTime.now(), linkInfo, new HashMap<>(), new HashMap<>());
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
        StackOverflowInfo that = (StackOverflowInfo) o;
        return Objects.equals(getLinkInfo(), that.getLinkInfo())
            && Objects.equals(getComments(), that.getComments())
            && Objects.equals(getAnswers(), that.getAnswers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLinkInfo());
    }

    public void setAnswersByCollection(Collection<StackOverflowAnswer> answers) {
        this.answers = answers.stream().collect(Collectors.toMap(i -> i.getIdAnswer(), i -> i));
    }

    public void setCommentsByCollection(Collection<StackOverflowComment> comments) {
        this.comments = comments.stream().collect(Collectors.toMap(i -> i.getIdComment(), i -> i));
    }
}
