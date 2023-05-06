package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl;

import ru.tinkoff.edu.java.scrapper.dataaccess.UpdateWebsiteInfoDAService;
import ru.tinkoff.edu.java.scrapper.dto.response.website.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareStackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.HandlerUpdateWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.BuilderLinkUpdateRequestStrategy;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.CompareInfoStrategy;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;

public class HandlerUpdateStackOverflowInfoChain extends HandlerUpdateWebsiteInfoImplChain<StackOverflowInfo, StackOverflowResponse, ResultOfCompareStackOverflowInfo>{
    private StackOverflowClient stackOverflowClient;

    public HandlerUpdateStackOverflowInfoChain(HandlerUpdateWebsiteInfo nextHandler, UpdateWebsiteInfoDAService<ResultOfCompareStackOverflowInfo> daService, CompareInfoStrategy<StackOverflowInfo, StackOverflowResponse, ResultOfCompareStackOverflowInfo> compareInfoStrategy, BuilderLinkUpdateRequestStrategy<ResultOfCompareStackOverflowInfo> builderLinkUpdateRequestStrategy, StackOverflowClient stackOverflowClient) {
        super(nextHandler, daService, compareInfoStrategy, builderLinkUpdateRequestStrategy);
        this.stackOverflowClient = stackOverflowClient;
    }

    public HandlerUpdateStackOverflowInfoChain(UpdateWebsiteInfoDAService<ResultOfCompareStackOverflowInfo> daService, CompareInfoStrategy<StackOverflowInfo, StackOverflowResponse, ResultOfCompareStackOverflowInfo> compareInfoStrategy, BuilderLinkUpdateRequestStrategy<ResultOfCompareStackOverflowInfo> builderLinkUpdateRequestStrategy, StackOverflowClient stackOverflowClient) {
        super(daService, compareInfoStrategy, builderLinkUpdateRequestStrategy);
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    protected StackOverflowResponse getWebsiteResponse(StackOverflowInfo savedWebsiteInfo) {
        return stackOverflowClient.getStackOverflowResponse(savedWebsiteInfo.getLinkInfo());
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
