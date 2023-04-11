package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import lombok.Getter;
import lombok.Setter;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public final class StackoverflowInfo extends WebsiteInfo{
    private Set<StackOverflowComment> comments;
    private Set<StackOverflowAnswer> answers;

    public StackoverflowInfo(LinkInfo linkInfo, Set<StackOverflowComment> comments, Set<StackOverflowAnswer> answers) {
        super(linkInfo);
        this.comments = comments;
        this.answers = answers;
    }

    public StackoverflowInfo(LinkInfo linkInfo) {
        this(linkInfo, new HashSet<>(), new HashSet<>());
    }
}
