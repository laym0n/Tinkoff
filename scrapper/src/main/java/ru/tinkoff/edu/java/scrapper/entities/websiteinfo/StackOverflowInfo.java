package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import lombok.*;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public final class StackOverflowInfo extends WebsiteInfo{
    private StackOverflowLinkInfo linkInfo;
    private Map<Integer, StackOverflowComment> comments;
    private Map<Integer, StackOverflowAnswer> answers;

    public StackOverflowInfo(int id, OffsetDateTime lastCheckUpdateDateTime, StackOverflowLinkInfo linkInfo,  Map<Integer, StackOverflowComment> comments,  Map<Integer, StackOverflowAnswer> answers) {
        super(id, lastCheckUpdateDateTime);
        this.linkInfo = linkInfo;
        this.comments = comments;
        this.answers = answers;
    }

    public StackOverflowInfo(StackOverflowLinkInfo linkInfo) {
        this(linkInfo, new HashMap<>(), new HashMap<>());
    }
}
