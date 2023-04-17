package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.stackoverflow;

import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.website.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.BuilderLinkUpdateRequestStrategy;

import java.net.URI;

public class StackOverflowBuilderLinkUpdateRequest implements BuilderLinkUpdateRequestStrategy<ResultOfCompareStackOverflowInfo> {
    @Override
    public LinkUpdateRequest buildLinkUpdateRequest(ResultOfCompareStackOverflowInfo changes, int[] chatIds) {
        LinkInfo linkInfo = changes.getLinkInfo();

        StringBuilder descriptionUpdate = new StringBuilder();
        descriptionUpdate.append("Ссылка " + linkInfo.getPath() + " получила обновление:\n");
        for(StackOverflowAnswerResponse answer : changes.getAddedAnswers()){
            descriptionUpdate.append(answer.getLastEditDate() + " - пользователь " +
                    answer.getOwner().getName() + " добавил новый ответ\n");
        }
        for(StackOverflowCommentResponse comment : changes.getAddedComments()){
            descriptionUpdate.append(comment.getCreatedAt() + " - пользователь  " + comment.getOwner().getName() +
                    " добавил новый коменарий\n");
        }
        for(StackOverflowAnswerResponse answer : changes.getEditedAnswers()){
            descriptionUpdate.append(answer.getLastEditDate() + " - пользователь " +
                    answer.getOwner().getName() + " отредактировал свой ответ\n");
        }

        LinkUpdateRequest result =
                new LinkUpdateRequest(changes.getIdWebsiteInfo(),
                        URI.create(linkInfo.getPath()),
                        descriptionUpdate.toString(),
                        chatIds);
        return result;
    }
}
