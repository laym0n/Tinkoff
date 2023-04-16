package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import lombok.*;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public final class GitHubInfo extends WebsiteInfo{
    private GitHubLinkInfo linkInfo;
    private Set<GitHubBranch> branches;
    private Set<GitHubCommit> commits;
    private OffsetDateTime lastActiveTime;

    public GitHubInfo(int id, OffsetDateTime lastCheckUpdateDateTime, GitHubLinkInfo linkInfo, OffsetDateTime lastActiveTime) {
        super(id, lastCheckUpdateDateTime);
        this.linkInfo = linkInfo;
        this.lastActiveTime = lastActiveTime;
        this.branches = new HashSet<>();
        this.commits = new HashSet<>();
    }

    public GitHubInfo(int id, OffsetDateTime lastCheckUpdateDateTime, GitHubLinkInfo linkInfo, Set<GitHubBranch> branches, Set<GitHubCommit> commits, OffsetDateTime lastActiveTime) {
        super(id, lastCheckUpdateDateTime);
        this.linkInfo = linkInfo;
        this.branches = branches;
        this.commits = commits;
        this.lastActiveTime = lastActiveTime;
    }

    public GitHubInfo(GitHubLinkInfo linkInfo, OffsetDateTime lastActiveTime) {
        this(linkInfo, new HashSet<>(), new HashSet<>(), lastActiveTime);
    }
}
