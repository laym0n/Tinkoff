package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import lombok.*;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public final class StackOverflowInfo extends WebsiteInfo{
    private StackOverflowLinkInfo linkInfo;
    private Set<StackOverflowComment> comments;
    private Set<StackOverflowAnswer> answers;

    public StackOverflowInfo(int id, OffsetDateTime lastCheckUpdateDateTime, StackOverflowLinkInfo linkInfo, Set<StackOverflowComment> comments, Set<StackOverflowAnswer> answers) {
        super(id, lastCheckUpdateDateTime);
        this.linkInfo = linkInfo;
        this.comments = comments;
        this.answers = answers;
    }

    public StackOverflowInfo(StackOverflowLinkInfo linkInfo) {
        this(linkInfo, new HashSet<>(), new HashSet<>());
    }
}
