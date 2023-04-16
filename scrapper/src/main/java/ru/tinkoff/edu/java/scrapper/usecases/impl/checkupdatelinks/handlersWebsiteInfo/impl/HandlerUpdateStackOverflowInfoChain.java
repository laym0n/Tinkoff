package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl;

import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.UpdateWebsiteInfoDAService;
import ru.tinkoff.edu.java.scrapper.dto.response.website.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.HandlerUpdateWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.BuilderLinkUpdateRequestStrategy;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.CompareInfoStrategy;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;

public class HandlerUpdateStackOverflowInfoChain extends HandlerUpdateWebsiteInfoImplChain<StackOverflowInfo, StackOverflowResponse>{
    private StackOverflowClient stackOverflowClient;

    public HandlerUpdateStackOverflowInfoChain(HandlerUpdateWebsiteInfo nextHandler, UpdateWebsiteInfoDAService<StackOverflowInfo, StackOverflowResponse> daService, CompareInfoStrategy<StackOverflowInfo, StackOverflowResponse> compareInfoStrategy, BuilderLinkUpdateRequestStrategy<StackOverflowInfo, StackOverflowResponse> builderLinkUpdateRequestStrategy, StackOverflowClient stackOverflowClient) {
        super(nextHandler, daService, compareInfoStrategy, builderLinkUpdateRequestStrategy);
        this.stackOverflowClient = stackOverflowClient;
    }

    public HandlerUpdateStackOverflowInfoChain(UpdateWebsiteInfoDAService<StackOverflowInfo, StackOverflowResponse> daService, CompareInfoStrategy<StackOverflowInfo, StackOverflowResponse> compareInfoStrategy, BuilderLinkUpdateRequestStrategy<StackOverflowInfo, StackOverflowResponse> builderLinkUpdateRequestStrategy, StackOverflowClient stackOverflowClient) {
        super(daService, compareInfoStrategy, builderLinkUpdateRequestStrategy);
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    protected StackOverflowResponse getWebsiteResponse(StackOverflowInfo savedWebsiteInfo) {
        StackOverflowResponse response = stackOverflowClient.getStackOverflowResponse(savedWebsiteInfo.getLinkInfo());
        return response;
    }

    @Override
    protected boolean canHandle(WebsiteInfo savedWebsiteInfo) {
        return (savedWebsiteInfo instanceof StackOverflowInfo);
    }

    @Override
    protected StackOverflowInfo getConcreteWebsiteInfo(WebsiteInfo savedWebsiteInfo) {
        return ((StackOverflowInfo) savedWebsiteInfo);
    }
}
