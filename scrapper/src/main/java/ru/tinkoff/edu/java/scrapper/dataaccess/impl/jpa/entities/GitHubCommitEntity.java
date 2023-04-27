package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.GitHubCommitPrimaryKey;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

@Entity
@NoArgsConstructor
@Data
@Table(name = "github_commit")
public class GitHubCommitEntity {
    @EmbeddedId
    private GitHubCommitPrimaryKey primaryKey;
    public GitHubCommitEntity(GitHubCommit commit, int gitHubInfoId){
        this.primaryKey = new GitHubCommitPrimaryKey(commit.getSha(), gitHubInfoId);
    }
    public GitHubCommit getGitHubCommit(){
        return new GitHubCommit(primaryKey.getSha());
    }
}
