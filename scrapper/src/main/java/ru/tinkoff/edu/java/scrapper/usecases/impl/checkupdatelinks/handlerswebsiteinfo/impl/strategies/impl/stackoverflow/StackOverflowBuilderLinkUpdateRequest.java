package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.impl.stackoverflow;

import org.springframework.stereotype.Component;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentResponse;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.BuilderLinkUpdateRequestStrategy;

import java.net.URI;

@Component
public class StackOverflowBuilderLinkUpdateRequest implements BuilderLinkUpdateRequestStrategy<ResultOfCompareStackOverflowInfo> {
    @Override
    public LinkUpdateRequest buildLinkUpdateRequest(ResultOfCompareStackOverflowInfo changes, int[] chatIds) {
        LinkInfo linkInfo = changes.getLinkInfo();

        StringBuilder descriptionUpdate = new StringBuilder();
        descriptionUpdate.append("Ссылка ").append(linkInfo.getPath())
                .append(" получила обновление:\n");
        for(StackOverflowAnswerResponse answer : changes.getAddedAnswers()){
            descriptionUpdate.append(answer.getLastEditDate()).append(" - пользователь ")
                    .append(answer.getOwner().getName()).append(" добавил новый ответ\n");
        }
        for(StackOverflowCommentResponse comment : changes.getAddedComments()){
            descriptionUpdate.append(comment.getCreatedAt()).append(" - пользователь  ")
                    .append(comment.getOwner().getName()).append(" добавил новый коменарий\n");
        }
        for(StackOverflowAnswerResponse answer : changes.getEditedAnswers()){
            descriptionUpdate.append(answer.getLastEditDate()).append(" - пользователь ")
                    .append(answer.getOwner().getName()).append(" отредактировал свой ответ\n");
        }

        return new LinkUpdateRequest(changes.getIdWebsiteInfo(),
                URI.create(linkInfo.getPath()),
                descriptionUpdate.toString(),
                chatIds);
    }
}
