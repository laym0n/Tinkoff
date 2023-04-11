package ru.tinkoff.edu.java.scrapper.webclients.githubclient;

import parserservice.dto.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubInfoResponse;
import ru.tinkoff.edu.java.scrapper.webclients.CheckerUpdateOfWebsite;

public interface GitHubClient extends CheckerUpdateOfWebsite {
    GitHubInfoResponse getUpdateInfo(GitHubInfo gitHubInfo);
}
