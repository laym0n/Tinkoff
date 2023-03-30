package ru.tinkoff.edu.java.scrapper.webclients.githubclient;

import parserservice.dto.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubInfoResponse;

public interface GitHubClient {
    GitHubInfoResponse getUpdateInfo(GitHubInfo gitHubInfo);
}
