package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "website_info_id")
@Table(name = "github_info")
public class GitHubInfoEntity extends WebsiteInfoEntity{
    @Column(name = "repository_name")
    private String repositoryName;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "last_activity_date_time")
    private Timestamp lastActiveTime;
    @OneToMany
    @JoinColumn(name = "website_info_id")
    private Collection<GitHubBranchEntity> branches;
    @OneToMany
    @JoinColumn(name = "website_info_id")
    private Collection<GitHubCommitEntity> commits;
    public GitHubInfoEntity(GitHubInfo gitHubInfo){
        super(gitHubInfo);
        this.repositoryName = gitHubInfo.getLinkInfo().repositoryName();
        this.userName = gitHubInfo.getLinkInfo().userName();
        this.lastActiveTime = Timestamp.valueOf(gitHubInfo.getLastActiveTime().toLocalDateTime());
        this.branches = gitHubInfo.getBranches().values().stream()
                .map(i-> new GitHubBranchEntity(i, gitHubInfo.getId())).collect(Collectors.toCollection(ArrayList::new));
        this.commits = gitHubInfo.getCommits().values().stream()
                .map(i-> new GitHubCommitEntity(i, gitHubInfo.getId())).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public GitHubInfo getWebsiteInfo() {
        Map<String, GitHubCommit> commitsForResult = commits.stream()
                .map(GitHubCommitEntity::getGitHubCommit)
                .collect(Collectors.toMap(GitHubCommit::getSha, i->i));
        Map<String, GitHubBranch> branchesForResult = branches.stream()
                .map(GitHubBranchEntity::getGitHubBranch)
                .collect(Collectors.toMap(GitHubBranch::getBranchName, i->i));
        GitHubInfo result = new GitHubInfo(
                id,
                OffsetDateTime.of(lastCheckUpdate.toLocalDateTime(), ZoneOffset.MIN),
                new GitHubLinkInfo(userName, repositoryName),
                branchesForResult,
                commitsForResult,
                OffsetDateTime.of(lastActiveTime.toLocalDateTime(), ZoneOffset.MIN)
        );
        return result;
    }
}
