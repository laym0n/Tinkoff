package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;

import java.io.Serializable;

@Embeddable
public class GitHubCommitPrimaryKey implements Serializable {
    private String sha;
    @Column(name = "website_info_id")
    @JoinColumn(table = "github_info",
    name = "website_info_id")
    private int gitHubInfoId;
}
