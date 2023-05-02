package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubBranchPrimaryKey implements Serializable {
    @Column(name = "name")
    private String name;
    @Column(name = "website_info_id")
    @JoinColumn(table = "github_info", columnDefinition = "website_info_id",
    referencedColumnName = "website_info_id")
    private int gitHubSiteId;
}
