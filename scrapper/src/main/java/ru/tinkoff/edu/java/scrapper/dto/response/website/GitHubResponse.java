package ru.tinkoff.edu.java.scrapper.dto.response.website;

import lombok.*;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubInfoResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public final class GitHubResponse implements WebsiteResponse{

    private GitHubInfoResponse infoResponse;
    private GitHubBranchResponse[] branches;
    private GitHubCommitResponse[] commits;
    public GitHubInfo getGitHubInfo(GitHubLinkInfo linkInfo){
        Set<GitHubBranch> newBranches = Arrays.stream(branches)
                .map(GitHubBranchResponse::getGitHubBranch)
                .collect(Collectors.toSet());
        Set<GitHubCommit> newCommits = Arrays.stream(commits)
                .map(GitHubCommitResponse::getGitHubCommit)
                .collect(Collectors.toSet());
        GitHubInfo result = new GitHubInfo(linkInfo, newBranches, newCommits, infoResponse.getUpdatedAt());
        return result;
    }
}