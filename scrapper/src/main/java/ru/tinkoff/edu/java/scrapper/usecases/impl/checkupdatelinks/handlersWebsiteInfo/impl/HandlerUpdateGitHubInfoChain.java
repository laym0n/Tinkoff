package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl;

import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dataaccess.UpdateWebsiteInfoDAService;
import ru.tinkoff.edu.java.scrapper.dto.response.website.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.HandlerUpdateWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.BuilderLinkUpdateRequestStrategy;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.CompareInfoStrategy;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;

public class HandlerUpdateGitHubInfoChain extends HandlerUpdateWebsiteInfoImplChain<GitHubInfo, GitHubResponse>{
    private GitHubClient gitHubClient;

    public HandlerUpdateGitHubInfoChain(HandlerUpdateWebsiteInfo nextHandler, UpdateWebsiteInfoDAService<GitHubInfo, GitHubResponse> daService, CompareInfoStrategy<GitHubInfo, GitHubResponse> compareInfoStrategy, BuilderLinkUpdateRequestStrategy<GitHubInfo, GitHubResponse> builderLinkUpdateRequestStrategy, GitHubClient gitHubClient) {
        super(nextHandler, daService, compareInfoStrategy, builderLinkUpdateRequestStrategy);
        this.gitHubClient = gitHubClient;
    }

    public HandlerUpdateGitHubInfoChain(UpdateWebsiteInfoDAService<GitHubInfo, GitHubResponse> daService, CompareInfoStrategy<GitHubInfo, GitHubResponse> compareInfoStrategy, BuilderLinkUpdateRequestStrategy<GitHubInfo, GitHubResponse> builderLinkUpdateRequestStrategy, GitHubClient gitHubClient) {
        super(daService, compareInfoStrategy, builderLinkUpdateRequestStrategy);
        this.gitHubClient = gitHubClient;
    }

    @Override
    protected GitHubResponse getWebsiteResponse(GitHubInfo savedWebsiteInfo) {
        GitHubResponse response = gitHubClient.getGitHubResponse(savedWebsiteInfo.getLinkInfo());
        return response;
    }

    @Override
    protected boolean canHandle(WebsiteInfo savedWebsiteInfo) {
        return (savedWebsiteInfo instanceof GitHubInfo);
    }

    @Override
    protected GitHubInfo getConcreteWebsiteInfo(WebsiteInfo savedWebsiteInfo) {
        return ((GitHubInfo) savedWebsiteInfo);
    }
}
