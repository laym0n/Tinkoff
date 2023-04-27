package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.embededids.GitHubBranchPrimaryKey;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;

@Data
@NoArgsConstructor
@Entity
@Table(name = "github_branch")
public class GitHubBranchEntity {
    @EmbeddedId
    private GitHubBranchPrimaryKey primaryKey;
    public GitHubBranchEntity(GitHubBranch branch, int gitHubInfoId){
        this.primaryKey = new GitHubBranchPrimaryKey(branch.getBranchName(), gitHubInfoId);
    }
    public GitHubBranch getGitHubBranch(){
        return new GitHubBranch(primaryKey.getName());
    }
}
