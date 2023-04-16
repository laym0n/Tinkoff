package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.github;

import ru.tinkoff.edu.java.scrapper.dto.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubInfoResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.CompareInfoStrategy;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CompareGitHubInfoStrategy implements CompareInfoStrategy<GitHubInfo, GitHubResponse> {
    @Override
    public ResultOfCompareWebsiteInfo<GitHubInfo, GitHubResponse> compare(GitHubInfo savedInfo, GitHubResponse loadedResponse) {
        Set<GitHubBranch> uniqueBranches = findUniqueBranchesForSaved(savedInfo, loadedResponse);
        Set<GitHubCommit> uniqueCommits = findUniqueCommitsForSaved(savedInfo, loadedResponse);
        OffsetDateTime uniqueLastActiveTime =
                (savedInfo.getLastActiveTime().equals(loadedResponse.getInfoResponse().getUpdatedAt())? null : savedInfo.getLastActiveTime());
        GitHubInfo uniqueInfoForSaved = new GitHubInfo(savedInfo.getId(), savedInfo.getLastCheckUpdateDateTime(),
                savedInfo.getLinkInfo(), uniqueBranches, uniqueCommits, uniqueLastActiveTime);

        OffsetDateTime uniqueCreatedAt = uniqueLastActiveTime == null? null : loadedResponse.getInfoResponse().getUpdatedAt();
        GitHubInfoResponse uniqueInfoResponse = new GitHubInfoResponse(uniqueCreatedAt);
        GitHubBranchResponse[] uniqueBranchesForResponse = findUniqueBranchesForLoaded(savedInfo, loadedResponse);
        GitHubCommitResponse[] uniqueCommitsForResponse = findUniqueCommitsForLoaded(savedInfo, loadedResponse);
        GitHubResponse uniqueInfoForResponse = new GitHubResponse(uniqueInfoResponse, uniqueBranchesForResponse,
                uniqueCommitsForResponse);

        boolean isDifferent = isDifferent(uniqueInfoForSaved, uniqueInfoForResponse);

        ResultOfCompareWebsiteInfo<GitHubInfo, GitHubResponse> result = new ResultOfCompareWebsiteInfo<>(isDifferent,
                uniqueInfoForSaved, uniqueInfoForResponse);
        return result;
    }
    private Set<GitHubBranch> findUniqueBranchesForSaved(GitHubInfo savedInfo, GitHubResponse loadedResponse){
        Set<String> loadedBranches = Arrays.stream(loadedResponse.getBranches())
                .map(branchResponse -> branchResponse.getName()).collect(Collectors.toSet());
        Set<GitHubBranch> branches = savedInfo.getBranches().stream()
                .filter(i->!loadedBranches.contains(i.getBranchName())).collect(Collectors.toSet());
        return branches;
    }
    private Set<GitHubCommit> findUniqueCommitsForSaved(GitHubInfo savedInfo, GitHubResponse loadedResponse){
        Set<String> loadedCommits = Arrays.stream(loadedResponse.getCommits())
                .map(commitResponse->commitResponse.getSha()).collect(Collectors.toSet());
        Set<GitHubCommit> uniqueCommits = savedInfo.getCommits().stream()
                .filter(commit -> !loadedCommits.contains(commit.getSha())).collect(Collectors.toSet());
        return uniqueCommits;
    }
    private GitHubBranchResponse[] findUniqueBranchesForLoaded(GitHubInfo savedInfo, GitHubResponse loadedResponse){
        GitHubBranchResponse[] result = Arrays.stream(loadedResponse.getBranches())
                .filter(branchResponse -> !savedInfo.getBranches().contains(branchResponse.getGitHubBranch()))
                .toArray(GitHubBranchResponse[]::new);
        return result;
    }
    private GitHubCommitResponse[] findUniqueCommitsForLoaded(GitHubInfo savedInfo, GitHubResponse loadedResponse){
        GitHubCommitResponse[] result = Arrays.stream(loadedResponse.getCommits())
                .filter(commitResponse -> !savedInfo.getCommits().contains(commitResponse.getGitHubCommit()))
                .toArray(GitHubCommitResponse[]::new);
        return result;
    }

    private boolean isDifferent(GitHubInfo uniqueInfo, GitHubResponse uniqueResponse){
        return (uniqueInfo.getBranches().size() != 0 || uniqueInfo.getLastActiveTime() != null ||
                uniqueInfo.getCommits().size() != 0 || uniqueResponse.getCommits().length != 0 ||
                uniqueResponse.getBranches().length != 0);
    }
}
