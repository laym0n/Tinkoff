package ru.tinkoff.edu.java.scrapper.webclients.githubclient;

import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerOfWebsiteInfo;

public interface GitHubClient extends CheckerOfWebsiteInfo {
    GitHubInfo getGitHubInfo(GitHubLinkInfo gitHubInfo);
    boolean checkIfGitHubLinkExist(GitHubLinkInfo gitHubInfo);
}
