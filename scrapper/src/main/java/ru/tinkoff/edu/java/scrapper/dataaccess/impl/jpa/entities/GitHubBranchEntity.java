package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.GitHubBranchPrimaryKey;

@Data
@Entity
@Table(name = "github_branch")
public class GitHubBranchEntity {
    @EmbeddedId
    private GitHubBranchPrimaryKey primaryKey;
}
