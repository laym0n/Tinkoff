package ru.tinkoff.edu.java.scrapper.webclients.githubclient;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import parserservice.dto.GitHubInfo;
import parserservice.dto.WebsiteInfo;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubInfoResponse;

import java.time.OffsetDateTime;

@Component
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
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(
                        new RuntimeException("Don't exist GitHub user with username "
                                + gitHubInfo.userName() +
                                " with repository name " + gitHubInfo.repositoryName())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(
                        new RuntimeException("Something wrong with GitHub")))
                .bodyToMono(GitHubInfoResponse.class).block();
        return gitHubInfoResponse;
    }

    @Override
    public OffsetDateTime checkUpdateOfWebsite(WebsiteInfo websiteInfo) {
        if(!(websiteInfo instanceof GitHubInfo))
            return null;
        GitHubInfo gitHubInfo = (GitHubInfo)websiteInfo;
        GitHubInfoResponse gitHubInfoResponse = getUpdateInfo(gitHubInfo);
        return gitHubInfoResponse.getUpdatedAt();
    }
}
