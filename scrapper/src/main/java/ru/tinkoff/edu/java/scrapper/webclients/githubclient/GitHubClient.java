package ru.tinkoff.edu.java.scrapper.webclients.githubclient;

import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.GitHubResponse;

public interface GitHubClient {
    GitHubResponse getGitHubResponse(GitHubLinkInfo gitHubInfo);
    boolean checkIfGitHubLinkExist(GitHubLinkInfo gitHubInfo);
}
