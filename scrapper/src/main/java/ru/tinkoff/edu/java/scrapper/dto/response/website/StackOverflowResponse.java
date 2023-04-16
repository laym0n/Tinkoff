package ru.tinkoff.edu.java.scrapper.dto.response.website;

import lombok.*;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.*;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public final class StackOverflowResponse implements WebsiteResponse{
    private StackOverflowAnswersResponse answers;
    private StackOverflowCommentsResponse comments;
    public StackOverflowInfo getStackOverflowInfo(StackOverflowLinkInfo linkInfo){
        Set<StackOverflowAnswer> newAnswers = Arrays.stream(answers.getItems())
                .map(StackOverflowAnswerResponse::getStackOverflowAnswer)
                .collect(Collectors.toSet());
        Set<StackOverflowComment> newComments = Arrays.stream(comments.getItems())
                .map(StackOverflowCommentResponse::getStackOverflowComment).collect(Collectors.toSet());
        StackOverflowInfo result = new StackOverflowInfo(linkInfo, newComments, newAnswers);
        return result;
    }
}