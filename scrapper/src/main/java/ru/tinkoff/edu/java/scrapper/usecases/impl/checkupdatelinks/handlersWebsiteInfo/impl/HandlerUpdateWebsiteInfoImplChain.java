package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.UpdateWebsiteInfoDAService;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.website.WebsiteResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.HandlerUpdateWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.BuilderLinkUpdateRequestStrategy;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.CompareInfoStrategy;

import java.util.Optional;

@AllArgsConstructor
public abstract class HandlerUpdateWebsiteInfoImplChain<W extends WebsiteInfo, R extends WebsiteResponse, C extends ResultOfCompareWebsiteInfo> implements HandlerUpdateWebsiteInfo {
    private HandlerUpdateWebsiteInfo nextHandler;
    private UpdateWebsiteInfoDAService<C> daService;
    private CompareInfoStrategy<W, R, C> compareInfoStrategy;
    private BuilderLinkUpdateRequestStrategy<C> builderLinkUpdateRequestStrategy;

    public HandlerUpdateWebsiteInfoImplChain(UpdateWebsiteInfoDAService<C> daService, CompareInfoStrategy<W, R, C> compareInfoStrategy, BuilderLinkUpdateRequestStrategy<C> builderLinkUpdateRequestStrategy) {
        this.daService = daService;
        this.compareInfoStrategy = compareInfoStrategy;
        this.builderLinkUpdateRequestStrategy = builderLinkUpdateRequestStrategy;
    }

    @Override
    public Optional<LinkUpdateRequest> updateWebsiteInfo(WebsiteInfo savedWebsiteInfo) {
        if(!canHandle(savedWebsiteInfo))
            return nextHandler.updateWebsiteInfo(savedWebsiteInfo);

        W savedConcreteWebsiteInfo = getConcreteWebsiteInfo(savedWebsiteInfo);
        R websiteResponse = getWebsiteResponse(savedConcreteWebsiteInfo);
        C resultOfCompare = compareInfoStrategy
                .compare(savedConcreteWebsiteInfo, websiteResponse);

        daService.applyChanges(resultOfCompare);
        LinkUpdateRequest linkUpdateRequest = null;
        if(resultOfCompare.isDifferent()){
            int[] chatIds = daService.getAllChatIdWithTrackedIdWebsiteInfo(savedWebsiteInfo.getId());
            linkUpdateRequest = builderLinkUpdateRequestStrategy
                    .buildLinkUpdateRequest(resultOfCompare, chatIds);

        }
        return Optional.ofNullable(linkUpdateRequest);
    }
    protected abstract R getWebsiteResponse(W savedWebsiteInfo);
    protected abstract boolean canHandle(WebsiteInfo savedWebsiteInfo);
    protected abstract W getConcreteWebsiteInfo(WebsiteInfo savedWebsiteInfo);
}
