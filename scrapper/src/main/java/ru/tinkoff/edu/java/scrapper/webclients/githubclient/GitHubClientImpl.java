package ru.tinkoff.edu.java.scrapper.webclients.githubclient;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import parserservice.dto.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubInfoResponse;

public class GitHubClientImpl implements GitHubClient {
    private String baseURL;
    public GitHubClientImpl(){
        this("https://api.github.com");
    }
    public GitHubClientImpl(String baseURL){
        this.baseURL = baseURL;
    }

    @Override
    public GitHubInfoResponse getUpdateInfo(GitHubInfo gitHubInfo) {
        WebClient webClient = WebClient.create(baseURL);
        GitHubInfoResponse gitHubInfoResponse = webClient
                .get().uri("/repos/{owner}/{repo}", gitHubInfo.userName(), gitHubInfo.repositoryName())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GitHubInfoResponse.class).block();
        return gitHubInfoResponse;
    }
}
