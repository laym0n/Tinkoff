package ru.tinkoff.edu.java.scrapper.webclients.githubclient;

import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.webclients.WebsiteInfoWebClient;

public interface GitHubClient extends WebsiteInfoWebClient {
    GitHubInfo getGitHubInfo(GitHubLinkInfo gitHubInfo);
    boolean checkIfGitHubLinkExist(GitHubLinkInfo gitHubInfo);
}
