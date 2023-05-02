package ru.tinkoff.edu.java.scrapper.dto.response.website;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswersResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentsResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public final class StackOverflowResponse implements WebsiteResponse {
    private StackOverflowAnswersResponse answers;
    private StackOverflowCommentsResponse comments;

    public StackOverflowInfo getStackOverflowInfo(StackOverflowLinkInfo linkInfo) {
        Map<Integer, StackOverflowAnswer> newAnswers = Arrays.stream(answers.getItems())
                .map(StackOverflowAnswerResponse::getStackOverflowAnswer)
                .collect(Collectors.toMap(StackOverflowAnswer::getIdAnswer, i -> i));
        Map<Integer, StackOverflowComment> newComments = Arrays.stream(comments.getItems())
                .map(StackOverflowCommentResponse::getStackOverflowComment)
                .collect(Collectors.toMap(StackOverflowComment::getIdComment, i -> i));
        StackOverflowInfo result = new StackOverflowInfo(0, OffsetDateTime.now(), linkInfo, newComments, newAnswers);
        return result;
    }
}
