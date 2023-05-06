package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubCommitPrimaryKey implements Serializable {
    @Column(name = "sha")
    private String sha;
    @Column(name = "website_info_id")
    @JoinColumn(table = "github_info",
    name = "website_info_id", referencedColumnName = "website_info_id")
    private int gitHubInfoId;
}
