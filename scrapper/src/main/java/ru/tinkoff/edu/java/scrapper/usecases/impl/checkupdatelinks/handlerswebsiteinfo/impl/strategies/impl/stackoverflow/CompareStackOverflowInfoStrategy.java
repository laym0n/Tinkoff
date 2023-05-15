package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.impl.stackoverflow;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dto.response.website.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.CompareInfoStrategy;

@Component
public class CompareStackOverflowInfoStrategy implements
    CompareInfoStrategy<StackOverflowInfo, StackOverflowResponse, ResultOfCompareStackOverflowInfo> {
    @Override
    public ResultOfCompareStackOverflowInfo compare(StackOverflowInfo savedInfo, StackOverflowResponse loadedResponse) {
        ResultOfCompareStackOverflowInfo result =
            new ResultOfCompareStackOverflowInfo(savedInfo.getId(), savedInfo.getLinkInfo());

        findDeletedAnswers(result, savedInfo, loadedResponse);
        findDeletedComments(result, savedInfo, loadedResponse);
        findAddedAndEditedAnswers(result, savedInfo, loadedResponse);
        findAddedComments(result, savedInfo, loadedResponse);

        return result;
    }

    private void findDeletedAnswers(ResultOfCompareStackOverflowInfo result,
                                    StackOverflowInfo savedInfo, StackOverflowResponse loadedResponse) {
        Set<Integer> loadedAnswers = Arrays.stream(loadedResponse.getAnswers().getItems())
                .map(StackOverflowAnswerResponse::getAnswerId).collect(Collectors.toSet());
        StackOverflowAnswer[] deletedAnswers = savedInfo.getAnswers().values().stream()
                .filter(savedAnswer -> !loadedAnswers.contains(savedAnswer.getIdAnswer()))
                .toArray(StackOverflowAnswer[]::new);
        result.setDeletedAnswers(deletedAnswers);
        if (deletedAnswers.length > 0) {
            result.setDifferent(true);
        }
    }

    private void findDeletedComments(ResultOfCompareStackOverflowInfo result,
                                            StackOverflowInfo savedInfo, StackOverflowResponse loadedResponse) {
        Set<Integer> loadedComments = Arrays.stream(loadedResponse.getComments().getItems())
                .map(StackOverflowCommentResponse::getIdComment).collect(Collectors.toSet());

        StackOverflowComment[] deletedComments = savedInfo.getComments().values().stream()
                .filter(savedComment -> !loadedComments.contains(savedComment.getIdComment()))
                .toArray(StackOverflowComment[]::new);
        result.setDeletedComments(deletedComments);
        if (deletedComments.length > 0) {
            result.setDifferent(true);
        }
    }

    private void findAddedAndEditedAnswers(ResultOfCompareStackOverflowInfo result,
                                           StackOverflowInfo savedInfo, StackOverflowResponse loadedResponse) {
        List<StackOverflowAnswerResponse> editedAnswers = new ArrayList<>();
        List<StackOverflowAnswerResponse> addedAnswers = new ArrayList<>();
        Arrays.stream(loadedResponse.getAnswers().getItems())
                .forEach(answerResponse -> {
                    StackOverflowAnswer savedAnswer = savedInfo.getAnswers().get(answerResponse.getAnswerId());
                    if (savedAnswer == null) {
                        addedAnswers.add(answerResponse);
                    } else if (!savedAnswer.getLastEditDate().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS)
                            .equals(
                                answerResponse
                                    .getLastEditDate()
                                    .toLocalDateTime()
                                    .truncatedTo(ChronoUnit.SECONDS))
                    ) {
                        editedAnswers.add(answerResponse);
                    }
                });
        result.setEditedAnswers(editedAnswers.toArray(StackOverflowAnswerResponse[]::new));
        result.setAddedAnswers(addedAnswers.toArray(StackOverflowAnswerResponse[]::new));
        if (!editedAnswers.isEmpty() || !addedAnswers.isEmpty()) {
            result.setDifferent(true);
        }
    }

    private void findAddedComments(ResultOfCompareStackOverflowInfo result,
                                   StackOverflowInfo savedInfo, StackOverflowResponse loadedResponse) {
        StackOverflowCommentResponse[] addedComments = Arrays.stream(loadedResponse.getComments().getItems())
                .filter(commentResponse -> !savedInfo.getComments().containsKey(commentResponse.getIdComment()))
                .toArray(StackOverflowCommentResponse[]::new);
        result.setAddedComments(addedComments);
        if (addedComments.length > 0) {
            result.setDifferent(true);
        }
    }
}
