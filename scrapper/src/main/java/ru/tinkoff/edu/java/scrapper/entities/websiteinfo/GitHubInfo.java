package ru.tinkoff.edu.java.scrapper.entities.websiteinfo;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsExclude;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

@Data
@ToString(callSuper = true)
public final class GitHubInfo extends WebsiteInfo {
    private GitHubLinkInfo linkInfo;
    private Map<String, GitHubBranch> branches;
    private Map<String, GitHubCommit> commits;
    @EqualsExclude
    private OffsetDateTime lastActiveTime;

    public GitHubInfo(
        int id,
        OffsetDateTime lastCheckUpdateDateTime,
        GitHubLinkInfo linkInfo,
        OffsetDateTime lastActiveTime
    ) {
        super(id, lastCheckUpdateDateTime);
        this.linkInfo = linkInfo;
        this.lastActiveTime = lastActiveTime;
        this.branches = new HashMap<>();
        this.commits = new HashMap<>();
    }

    public GitHubInfo(
        int id,
        OffsetDateTime lastCheckUpdateDateTime,
        GitHubLinkInfo linkInfo,
        Map<String, GitHubBranch> branches,
        Map<String, GitHubCommit> commits,
        OffsetDateTime lastActiveTime
    ) {
        super(id, lastCheckUpdateDateTime);
        this.linkInfo = linkInfo;
        this.branches = branches;
        this.commits = commits;
        this.lastActiveTime = lastActiveTime;
    }

    public GitHubInfo(GitHubLinkInfo linkInfo, OffsetDateTime lastActiveTime) {
        this(0, OffsetDateTime.now(), linkInfo, new HashMap<>(), new HashMap<>(), lastActiveTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        GitHubInfo that = (GitHubInfo) o;
        return getLinkInfo().equals(that.getLinkInfo())
            && getBranches().equals(that.getBranches())
            && getCommits().equals(that.getCommits());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLinkInfo());
    }

    public void setCommitsByCollection(Collection<GitHubCommit> commits) {
        this.commits = commits.stream().collect(Collectors.toMap(i -> i.getSha(), i -> i));
    }

    public void setBranchesByCollection(Collection<GitHubBranch> branches) {
        this.branches = branches.stream().collect(Collectors.toMap(i -> i.getBranchName(), i -> i));
    }
}
