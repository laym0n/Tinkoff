package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;

import java.io.Serializable;

@Embeddable
public class GitHubBranchPrimaryKey implements Serializable {
    private String name;
    @Column(name = "website_info_id")
    @JoinColumn(table = "github_info", columnDefinition = "website_info_id")
    private int gitHubSiteId;
}
