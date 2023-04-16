package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.impl.github;

import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.dto.ResultOfCompareWebsiteInfo;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.website.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubBranchResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.website.github.GitHubCommitResponse;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.handlersWebsiteInfo.impl.strategies.BuilderLinkUpdateRequestStrategy;

import java.net.URI;

public class GitHubBuilderLinkUpdateRequest implements BuilderLinkUpdateRequestStrategy<GitHubInfo, GitHubResponse> {
    @Override
    public LinkUpdateRequest buildLinkUpdateRequest(ResultOfCompareWebsiteInfo<GitHubInfo, GitHubResponse> changes, int[] chatIds) {
        LinkInfo linkInfo = changes.uniqueSavedData.getLinkInfo();

        StringBuilder descriptionUpdate = new StringBuilder();
        descriptionUpdate.append("Ссылка " + linkInfo.getPath() + " получила обновление:\n");
        for(GitHubBranch branch : changes.uniqueSavedData.getBranches()){
            descriptionUpdate.append("Ветка " + branch.getBranchName() + " удалена\n");
        }
        for(GitHubCommit commit : changes.uniqueSavedData.getCommits()){
            descriptionUpdate.append("Коммит с sha " + commit.getSha() + " удален\n");
        }
        for(GitHubBranchResponse branch : changes.uniqueLoadedData.getBranches()){
            descriptionUpdate.append("Ветка " + branch.getName() + " добавлена\n");
        }
        for(GitHubCommitResponse commit : changes.uniqueLoadedData.getCommits()){
            descriptionUpdate.append("Коммит с sha " + commit.getSha() + " добавлен\n");
        }

        LinkUpdateRequest result =
                new LinkUpdateRequest(changes.uniqueSavedData.getId(),
                        URI.create(linkInfo.getPath()),
                        descriptionUpdate.toString(),
                        chatIds);
        return result;
    }
}
