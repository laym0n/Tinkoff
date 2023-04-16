package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.stackoverflow;

import ru.tinkoff.edu.java.scrapper.dto.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswersResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentsResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.CompareInfoStrategy;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CompareStackOverflowInfoStrategy implements CompareInfoStrategy<StackOverflowInfo, StackOverflowResponse> {
    @Override
    public ResultOfCompareWebsiteInfo<StackOverflowInfo, StackOverflowResponse> compare(StackOverflowInfo savedInfo, StackOverflowResponse loadedResponse) {
        Set<StackOverflowAnswer> uniqueAnswers = findUniqueAnswersForSaved(savedInfo, loadedResponse);
        Set<StackOverflowComment> uniqueComments = findUniqueCommentsForSaved(savedInfo, loadedResponse);
        StackOverflowInfo uniqueInfo = new StackOverflowInfo(savedInfo.getId(),
                savedInfo.getLastCheckUpdateDateTime(), savedInfo.getLinkInfo(), uniqueComments, uniqueAnswers);

        StackOverflowAnswersResponse uniqueAnswersForLoaded = findUniqueAnswersForLoaded(savedInfo, loadedResponse);
        StackOverflowCommentsResponse uniqueCommentsForLoaded = findUniqueCommentsForLoaded(savedInfo, loadedResponse);
        StackOverflowResponse uniqueResponse = new StackOverflowResponse(
                uniqueAnswersForLoaded,
                uniqueCommentsForLoaded
                );

        boolean isDifferent = isDifferent(uniqueInfo, uniqueResponse);
        ResultOfCompareWebsiteInfo<StackOverflowInfo, StackOverflowResponse> result =
                new ResultOfCompareWebsiteInfo<>(isDifferent, uniqueInfo, uniqueResponse);
        return result;
    }
    private Set<StackOverflowAnswer> findUniqueAnswersForSaved(StackOverflowInfo savedInfo, StackOverflowResponse loadedResponse){
        Set<Integer> loadedAnswers = Arrays.stream(loadedResponse.getAnswers().getItems())
                .map(answerResponse -> answerResponse.getAnswerId()).collect(Collectors.toSet());
        Set<StackOverflowAnswer> uniqueAnswers = savedInfo.getAnswers().stream()
                .filter(savedAnswer -> !loadedAnswers.contains(savedAnswer.getIdAnswer()))
                .collect(Collectors.toSet());
        return uniqueAnswers;
    }
    private Set<StackOverflowComment> findUniqueCommentsForSaved(StackOverflowInfo savedInfo, StackOverflowResponse loadedResponse){
        Set<Integer> loadedComments = Arrays.stream(loadedResponse.getComments().getItems())
                .map(loadedComment -> loadedComment.getIdComment()).collect(Collectors.toSet());

        Set<StackOverflowComment> uniqueComments = savedInfo.getComments().stream()
                .filter(savedComment -> !loadedComments.contains(savedComment.getIdComment()))
                .collect(Collectors.toSet());
        return uniqueComments;
    }
    private StackOverflowAnswersResponse findUniqueAnswersForLoaded(StackOverflowInfo savedInfo, StackOverflowResponse loadedResponse){
        StackOverflowAnswerResponse[] uniqueAnswers = Arrays.stream(loadedResponse.getAnswers().getItems())
                .filter(answerResponse -> !savedInfo.getAnswers().contains(answerResponse.getStackOverflowAnswer()))
                .toArray(StackOverflowAnswerResponse[]::new);
        StackOverflowAnswersResponse result = new StackOverflowAnswersResponse(uniqueAnswers);
        return result;
    }
    private StackOverflowCommentsResponse findUniqueCommentsForLoaded(StackOverflowInfo savedInfo, StackOverflowResponse loadedResponse){
        StackOverflowCommentResponse[] uniqueComments = Arrays.stream(loadedResponse.getComments().getItems())
                .filter(commentResponse -> !savedInfo.getComments().contains(commentResponse.getStackOverflowComment()))
                .toArray(StackOverflowCommentResponse[]::new);
        StackOverflowCommentsResponse result = new StackOverflowCommentsResponse(uniqueComments);
        return result;
    }
    private boolean isDifferent(StackOverflowInfo savedInfo, StackOverflowResponse loadedResponse){
        return (savedInfo.getComments().size() != 0 || savedInfo.getAnswers().size() != 0 ||
                loadedResponse.getComments().getItems().length != 0 || loadedResponse.getAnswers().getItems().length != 0);
    }
}
