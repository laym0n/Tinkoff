package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.github;

import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareGitHubInfo;
import ru.tinkoff.edu.java.scrapper.dto.resultofcomparewebsiteinfo.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.website.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.BuilderLinkUpdateRequestStrategy;

import java.net.URI;

public class GitHubBuilderLinkUpdateRequest implements BuilderLinkUpdateRequestStrategy<ResultOfCompareGitHubInfo> {
    @Override
    public LinkUpdateRequest buildLinkUpdateRequest(ResultOfCompareGitHubInfo changes, int[] chatIds) {
        LinkInfo linkInfo = changes.getLinkInfo();

        StringBuilder descriptionUpdate = new StringBuilder();
        descriptionUpdate.append("Ссылка " + linkInfo.getPath() + " получила обновление:\n");
        for(GitHubBranch branch : changes.getDroppedBranches()){
            descriptionUpdate.append("Ветка " + branch.getBranchName() + " удалена\n");
        }
        for(GitHubCommit commit : changes.getDroppedCommits()){
            descriptionUpdate.append("Коммит с sha " + commit.getSha() + " удален\n");
        }
        for(GitHubBranchResponse branch : changes.getAddedBranches()){
            descriptionUpdate.append("Ветка " + branch.getName() + " добавлена\n");
        }
        for(GitHubCommitResponse commit : changes.getPushedCommits()){
            descriptionUpdate.append(commit.getCommit().getCommitter().getDate() + " - коммит с sha " +
                    commit.getSha() + " добавлен\n");
        }
        if(changes.getLastActivityDate().isPresent())
            descriptionUpdate.append("Время последней активности " + changes.getLastActivityDate().get() + " получила обновление:\n");

        LinkUpdateRequest result =
                new LinkUpdateRequest(changes.getIdWebsiteInfo(),
                        URI.create(linkInfo.getPath()),
                        descriptionUpdate.toString(),
                        chatIds);
        return result;
    }
}
