package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public final class StackoverflowInfo extends WebsiteInfo{
    private StackOverflowLinkInfo linkInfo;
    private Set<StackOverflowComment> comments;
    private Set<StackOverflowAnswer> answers;
}
