package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.GitHubCommitPrimaryKey;

@Entity
@Data
@Table(name = "github_commit")
public class GitHubCommitEntity {
    @EmbeddedId
    private GitHubCommitPrimaryKey primaryKey;
}
