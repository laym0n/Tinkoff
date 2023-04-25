package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Data
public class GitHubInfoEntity{
    @Id
    @Column(name = "website_info_id")
    private int websiteId;
    @Column(name = "repository_name")
    private String repositoryName;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "last_activity_date_time")
    private OffsetDateTime lastActivity;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "website_info_id", referencedColumnName = "id")
    private WebsiteInfoEntity websiteInfoEntity;
    @OneToMany
    @JoinColumn(name = "website_info_id")
    private Set<GitHubBranchEntity> branches;
    @OneToMany
    @JoinColumn(name = "website_info_id")
    private Set<GitHubCommitEntity> commits;

}
