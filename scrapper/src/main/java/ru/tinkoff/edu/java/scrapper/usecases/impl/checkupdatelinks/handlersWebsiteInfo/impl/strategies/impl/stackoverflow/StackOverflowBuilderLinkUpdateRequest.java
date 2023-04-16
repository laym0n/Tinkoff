package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.stackoverflow;

import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.website.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowAnswerResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow.StackOverflowCommentResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.BuilderLinkUpdateRequestStrategy;

import java.net.URI;

public class StackOverflowBuilderLinkUpdateRequest implements BuilderLinkUpdateRequestStrategy<StackOverflowInfo, StackOverflowResponse> {
    @Override
    public LinkUpdateRequest buildLinkUpdateRequest(ResultOfCompareWebsiteInfo<StackOverflowInfo, StackOverflowResponse> changes, int[] chatIds) {
        LinkInfo linkInfo = changes.uniqueSavedData.getLinkInfo();

        StringBuilder descriptionUpdate = new StringBuilder();
        descriptionUpdate.append("Ссылка " + linkInfo.getPath() + " получила обновление:\n");
        for(StackOverflowAnswerResponse answer : changes.uniqueLoadedData.getAnswers().getItems()){
            descriptionUpdate.append("Пользователь " + answer.getOwner().getName() + " добавил новый ответ " +
                    ());
        }
        for(StackOverflowCommentResponse comment : changes.uniqueLoadedData.getComments().getItems()){
            descriptionUpdate.append("Пользователь  " + comment.getOwner().getName() + " добавил новый коменарий в " +
                    comment.getCreatedAt());
        }

        LinkUpdateRequest result =
                new LinkUpdateRequest(changes.uniqueSavedData.getId(),
                        URI.create(linkInfo.getPath()),
                        descriptionUpdate.toString(),
                        chatIds);
        return result;
    }
}
