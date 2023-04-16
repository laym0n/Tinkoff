package ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.impl.chainresponsobility;

import lombok.AllArgsConstructor;
import parserservice.dto.LinkInfo;
import parserservice.dto.StackOverflowLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.StackOverflowInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.webclients.stackoverflowclient.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.WebsiteInfoWebClient;


@AllArgsConstructor
public class StackOverflowChainWebClient extends WebsiteInfoWebClientChainImpl{
    private StackOverflowClient stackOverflowClient;

    public StackOverflowChainWebClient(WebsiteInfoWebClient nextWebsiteInfoWebClient, StackOverflowClient stackOverflowClient) {
        super(nextWebsiteInfoWebClient);
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    protected boolean canLoad(LinkInfo linkInfo) {
        return (linkInfo instanceof StackOverflowLinkInfo);
    }

    @Override
    protected WebsiteInfo loadWebsiteInfo(LinkInfo linkInfo) {
        StackOverflowLinkInfo stackOverflowLinkInfo = (StackOverflowLinkInfo) linkInfo;
        StackOverflowResponse response = stackOverflowClient.getStackOverflowResponse(stackOverflowLinkInfo);
        StackOverflowInfo result = response.getStackOverflowInfo(stackOverflowLinkInfo);
        return result;
    }
}
