package ru.tinkoff.edu.java.scrapper.webclients.githubclient;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import parserservice.dto.GitHubLinkInfo;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.response.website.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubInfoResponse;

@Component
public class GitHubClientImpl implements GitHubClient {
    private  WebClient webClient;
    public GitHubClientImpl(){
        this("https://api.github.com");
    }
    public GitHubClientImpl(String baseURL){
        webClient = WebClient.create(baseURL);
    }

    @Override
    public GitHubResponse getGitHubResponse(GitHubLinkInfo gitHubInfo) {
        GitHubInfoResponse infoResponse = getGitHubInfoResponse(gitHubInfo);
        GitHubBranchResponse[] branches = getArrayOfGitHubBranches(gitHubInfo);
        GitHubCommitResponse[] commits = getArrayOfGitHubCommitResponses(gitHubInfo);

        GitHubResponse result = new GitHubResponse(infoResponse, branches, commits);
        return result;
    }

    @Override
    public boolean checkIfGitHubLinkExist(GitHubLinkInfo gitHubInfo) {
        throw new NotImplementedException();
    }

    private GitHubBranchResponse[] getArrayOfGitHubBranches(GitHubLinkInfo gitHubInfo){
        GitHubBranchResponse[] gitHubBranchResponses = webClient
                .get().uri("/repos/{owner}/{repo}/branches", gitHubInfo.userName(), gitHubInfo.repositoryName())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.empty())
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(
                        new RuntimeException("Something wrong with GitHub")))
                .bodyToMono(GitHubBranchResponse[].class).block();
        return gitHubBranchResponses;
    }

    private GitHubCommitResponse[] getArrayOfGitHubCommitResponses(GitHubLinkInfo gitHubInfo){
        GitHubCommitResponse[] gitHubCommitResponses = webClient
                .get().uri("/repos/{owner}/{repo}/commits", gitHubInfo.userName(), gitHubInfo.repositoryName())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.empty())
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(
                        new RuntimeException("Something wrong with GitHub")))
                .bodyToMono(GitHubCommitResponse[].class).block();
        return gitHubCommitResponses;
    }

    private GitHubInfoResponse getGitHubInfoResponse(GitHubLinkInfo gitHubInfo){
        GitHubInfoResponse gitHubInfoResponse = webClient
                .get().uri("/repos/{owner}/{repo}", gitHubInfo.userName(), gitHubInfo.repositoryName())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.empty())
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(
                        new RuntimeException("Something wrong with GitHub")))
                .bodyToMono(GitHubInfoResponse.class).block();
        return gitHubInfoResponse;
    }
}
