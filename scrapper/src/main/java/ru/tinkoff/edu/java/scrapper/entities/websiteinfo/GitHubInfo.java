package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import lombok.Getter;
import parserservice.dto.GitHubLinkInfo;
import parserservice.dto.LinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
public final class GitHubInfo extends WebsiteInfo{
    private Set<GitHubBranch> branches;
    private Set<GitHubCommit> commits;
    private OffsetDateTime lastActiveTime;

    public GitHubInfo(LinkInfo linkInfo, Set<GitHubBranch> branches, Set<GitHubCommit> commits, OffsetDateTime lastActiveTime) {
        super(linkInfo);
        this.branches = branches;
        this.commits = commits;
        this.lastActiveTime = lastActiveTime;
    }

    public GitHubInfo(GitHubLinkInfo linkInfo, OffsetDateTime lastActiveTime) {
        this(linkInfo, new HashSet<>(), new HashSet<>(), lastActiveTime);
    }
}
