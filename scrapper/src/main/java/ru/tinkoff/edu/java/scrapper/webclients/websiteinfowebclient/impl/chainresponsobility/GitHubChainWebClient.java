package ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.impl.chainresponsobility;

import lombok.AllArgsConstructor;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.webclients.githubclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclients.websiteinfowebclient.WebsiteInfoWebClient;


@AllArgsConstructor
public class GitHubChainWebClient extends WebsiteInfoWebClientChainImpl{
    private GitHubClient gitHubClient;

    public GitHubChainWebClient(WebsiteInfoWebClient nextWebsiteInfoWebClient, GitHubClient gitHubClient) {
        super(nextWebsiteInfoWebClient);
        this.gitHubClient = gitHubClient;
    }

    @Override
    protected boolean canLoad(LinkInfo linkInfo) {
        return (linkInfo instanceof GitHubLinkInfo);
    }

    @Override
    protected WebsiteInfo loadWebsiteInfo(LinkInfo linkInfo) {
        GitHubLinkInfo gitHubLinkInfo = (GitHubLinkInfo) linkInfo;
        GitHubResponse response = gitHubClient.getGitHubResponse(gitHubLinkInfo);
        return response.getGitHubInfo(gitHubLinkInfo);
    }
}
