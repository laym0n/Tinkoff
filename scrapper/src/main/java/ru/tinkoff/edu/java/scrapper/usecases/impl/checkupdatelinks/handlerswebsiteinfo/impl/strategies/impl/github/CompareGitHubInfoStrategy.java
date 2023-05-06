package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.impl.github;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;
import ru.tinkoff.edu.java.scrapper.dto.response.website.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.CompareInfoStrategy;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CompareGitHubInfoStrategy implements CompareInfoStrategy<GitHubInfo, GitHubResponse, ResultOfCompareGitHubInfo> {
    @Override
    public ResultOfCompareGitHubInfo compare(GitHubInfo savedInfo, GitHubResponse loadedResponse) {
        ResultOfCompareGitHubInfo result = new ResultOfCompareGitHubInfo(savedInfo.getId(), savedInfo.getLinkInfo());

        findDropedBranches(result, savedInfo, loadedResponse);
        findDroppedCommits(result, savedInfo, loadedResponse);
        findAddedBranches(result, savedInfo, loadedResponse);
        findPushedCommits(result, savedInfo, loadedResponse);
        Optional<OffsetDateTime> lastActiveTime = Optional.ofNullable(
                (savedInfo.getLastActiveTime().equals(loadedResponse.getInfoResponse().getUpdatedAt()) ? null : loadedResponse.getInfoResponse().getUpdatedAt())
        );
        result.setLastActivityDate(lastActiveTime);
        return result;
    }
    private void findDropedBranches(ResultOfCompareGitHubInfo result, GitHubInfo savedInfo,
                                    GitHubResponse loadedResponse){
        Set<String> loadedBranches = Arrays.stream(loadedResponse.getBranches())
                .map(GitHubBranchResponse::getName).collect(Collectors.toSet());
        GitHubBranch[] droppedBranches = savedInfo.getBranches().values().stream()
                .filter(i->!loadedBranches.contains(i.getBranchName()))
                .toArray(GitHubBranch[]::new);
        result.setDroppedBranches(droppedBranches);
        if(droppedBranches.length > 0)
            result.setDifferent(true);
    }
    private void findDroppedCommits(ResultOfCompareGitHubInfo result, GitHubInfo savedInfo,
                                    GitHubResponse loadedResponse){
        Set<String> loadedCommits = Arrays.stream(loadedResponse.getCommits())
                .map(GitHubCommitResponse::getSha).collect(Collectors.toSet());
        GitHubCommit[] droppedCommits = savedInfo.getCommits().values().stream()
                .filter(commit -> !loadedCommits.contains(commit.getSha()))
                .toArray(GitHubCommit[]::new);
        result.setDroppedCommits(droppedCommits);
        if(droppedCommits.length > 0)
            result.setDifferent(true);
    }
    private void findAddedBranches(ResultOfCompareGitHubInfo result, GitHubInfo savedInfo,
                                   GitHubResponse loadedResponse){
        GitHubBranchResponse[] addedBranches = Arrays.stream(loadedResponse.getBranches())
                .filter(branchResponse -> !savedInfo.getBranches().containsKey(branchResponse.getName()))
                .toArray(GitHubBranchResponse[]::new);
        result.setAddedBranches(addedBranches);
        if(addedBranches.length > 0)
            result.setDifferent(true);
    }
    private void findPushedCommits(ResultOfCompareGitHubInfo result, GitHubInfo savedInfo,
                                   GitHubResponse loadedResponse){
        GitHubCommitResponse[] pushedCommits = Arrays.stream(loadedResponse.getCommits())
                .filter(commitResponse -> !savedInfo.getCommits().containsKey(commitResponse.getSha()))
                .toArray(GitHubCommitResponse[]::new);
        result.setPushedCommits(pushedCommits);
        if(pushedCommits.length > 0)
            result.setDifferent(true);
    }
}
