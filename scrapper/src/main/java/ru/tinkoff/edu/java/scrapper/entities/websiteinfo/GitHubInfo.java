package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import lombok.*;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public final class GitHubInfo extends WebsiteInfo{
    private GitHubLinkInfo linkInfo;
    private Map<String, GitHubBranch> branches;
    private Map<String, GitHubCommit> commits;
    private OffsetDateTime lastActiveTime;

    public GitHubInfo(int id, OffsetDateTime lastCheckUpdateDateTime, GitHubLinkInfo linkInfo, OffsetDateTime lastActiveTime) {
        super(id, lastCheckUpdateDateTime);
        this.linkInfo = linkInfo;
        this.lastActiveTime = lastActiveTime;
        this.branches = new HashMap<>();
        this.commits = new HashMap<>();
    }

    public GitHubInfo(int id, OffsetDateTime lastCheckUpdateDateTime, GitHubLinkInfo linkInfo, Map<String, GitHubBranch> branches, Map<String, GitHubCommit> commits, OffsetDateTime lastActiveTime) {
        super(id, lastCheckUpdateDateTime);
        this.linkInfo = linkInfo;
        this.branches = branches;
        this.commits = commits;
        this.lastActiveTime = lastActiveTime;
    }

    public GitHubInfo(GitHubLinkInfo linkInfo, OffsetDateTime lastActiveTime) {
        this(linkInfo, new HashMap<>(), new HashMap<>(), lastActiveTime);
    }
}
