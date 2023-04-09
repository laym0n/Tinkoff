package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
public final class GitHubInfo extends WebsiteInfo{
    private GitHubLinkInfo linkInfo;
    private Set<GitHubBranch> branches;
    private Set<GitHubCommit> commits;
    private OffsetDateTime lastActiveTime;
    public GitHubInfo(GitHubLinkInfo linkInfo, OffsetDateTime lastActiveTime) {
        this(linkInfo, new HashSet<>(), new HashSet<>(), lastActiveTime);
    }
}
