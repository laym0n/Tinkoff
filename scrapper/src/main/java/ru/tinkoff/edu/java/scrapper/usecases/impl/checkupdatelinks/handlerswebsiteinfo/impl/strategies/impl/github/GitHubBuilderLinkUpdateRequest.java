package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.impl.github;

import org.springframework.stereotype.Component;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlerswebsiteinfo.impl.strategies.BuilderLinkUpdateRequestStrategy;

import java.net.URI;


@Component
public class GitHubBuilderLinkUpdateRequest implements BuilderLinkUpdateRequestStrategy<ResultOfCompareGitHubInfo> {
    @Override
    public LinkUpdateRequest buildLinkUpdateRequest(ResultOfCompareGitHubInfo changes, int[] chatIds) {
        LinkInfo linkInfo = changes.getLinkInfo();

        StringBuilder descriptionUpdate = new StringBuilder();
        descriptionUpdate.append("Ссылка ").append(linkInfo.getPath()).append(" получила обновление:\n");
        for(GitHubBranch branch : changes.getDroppedBranches()){
            descriptionUpdate.append("Ветка ").append(branch.getBranchName()).append(" удалена\n");
        }
        for(GitHubCommit commit : changes.getDroppedCommits()){
            descriptionUpdate.append("Коммит с sha ").append(commit.getSha()).append(" удален\n");
        }
        for(GitHubBranchResponse branch : changes.getAddedBranches()){
            descriptionUpdate.append("Ветка ").append(branch.getName()).append(" добавлена\n");
        }
        for(GitHubCommitResponse commit : changes.getPushedCommits()){
            descriptionUpdate.append(commit.getCommit().getCommitter().getDate()).append(" - коммит с sha ").append(commit.getSha()).append(" добавлен\n");
        }
        if(changes.getLastActivityDate().isPresent())
            descriptionUpdate.append("Время последней активности ").append(changes.getLastActivityDate().get()).append(" получила обновление:\n");

        return new LinkUpdateRequest(changes.getIdWebsiteInfo(),
                        URI.create(linkInfo.getPath()),
                        descriptionUpdate.toString(),
                        chatIds);
    }
}
