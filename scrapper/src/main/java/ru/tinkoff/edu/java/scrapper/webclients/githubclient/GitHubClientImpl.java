package ru.tinkoff.edu.java.scrapper.webclients.githubclient;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.response.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.github.GitHubInfoResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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
    public GitHubInfo getGitHubInfo(GitHubLinkInfo gitHubInfo) {
        GitHubInfoResponse gitHubInfoResponse = getGitHubInfoResponse(gitHubInfo);
        if(gitHubInfoResponse == null)
            return null;
        GitHubBranchResponse[] gitHubBranchResponses = getArrayOfGitHubBranches(gitHubInfo);
        GitHubCommitResponse[] gitHubCommitResponses = getArrayOfGitHubCommitResponses(gitHubInfo);

        GitHubInfo result = createGitHubInfo(gitHubInfo, gitHubInfoResponse, gitHubBranchResponses, gitHubCommitResponses);

        return result;
    }

    @Override
    public boolean checkIfGitHubLinkExist(GitHubLinkInfo gitHubInfo) {
        return false;
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
                .get().uri("/repos/{owner}/{repo}/branches", gitHubInfo.userName(), gitHubInfo.repositoryName())
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

    private GitHubInfo createGitHubInfo(GitHubLinkInfo gitHubInfo,
                                        GitHubInfoResponse gitHubInfoResponse,
                                        GitHubBranchResponse[] gitHubBranchResponses,
                                        GitHubCommitResponse[] gitHubCommitResponses){
        Set<GitHubBranch> branches = Arrays.stream(gitHubBranchResponses)
                .map(gitHubBranchResponse -> new GitHubBranch(gitHubBranchResponse.name())).collect(Collectors.toSet());
        Set<GitHubCommit> commits = Arrays.stream(gitHubCommitResponses)
                .map(gitHubCommitResponse -> {
                    return  new GitHubCommit(gitHubCommitResponse.sha(), gitHubCommitResponse.commit().committer().date());
                }).collect(Collectors.toSet());
        GitHubInfo result = new GitHubInfo(gitHubInfo, branches, commits, gitHubInfoResponse.getUpdatedAt());
        return result;
    }
//    @Override
//    public OffsetDateTime checkUpdateOfWebsite(LinkInfo linkInfo) {
//        if(!(linkInfo instanceof GitHubLinkInfo))
//            return null;
//        GitHubLinkInfo gitHubInfo = (GitHubLinkInfo) linkInfo;
//        GitHubInfoResponse gitHubInfoResponse = getGitHubInfo(gitHubInfo);
//        return gitHubInfoResponse.getUpdatedAt();
//    }

    @Override
    public WebsiteInfo getWebSiteInfoByLinkInfo(LinkInfo linkInfo) {
        if(!(linkInfo instanceof GitHubLinkInfo))
            return null;
        return getGitHubInfo((GitHubLinkInfo) linkInfo);
    }
}
