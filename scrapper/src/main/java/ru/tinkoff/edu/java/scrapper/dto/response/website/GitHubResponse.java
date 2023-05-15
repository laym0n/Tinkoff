package ru.tinkoff.edu.java.scrapper.dto.response.website;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubInfoResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public final class GitHubResponse implements WebsiteResponse {

    private GitHubInfoResponse infoResponse;
    private GitHubBranchResponse[] branches;
    private GitHubCommitResponse[] commits;

    public GitHubInfo getGitHubInfo(GitHubLinkInfo linkInfo) {
        Map<String, GitHubBranch> newBranches = Arrays.stream(branches)
                .map(GitHubBranchResponse::getGitHubBranch)
                .collect(Collectors.toMap(GitHubBranch::getBranchName, i -> i));
        Map<String, GitHubCommit> newCommits = Arrays.stream(commits)
                .map(GitHubCommitResponse::getGitHubCommit)
                .collect(Collectors.toMap(GitHubCommit::getSha, i -> i));
        return new GitHubInfo(0, OffsetDateTime.now(), linkInfo, newBranches, newCommits, infoResponse.getUpdatedAt());
    }
}
