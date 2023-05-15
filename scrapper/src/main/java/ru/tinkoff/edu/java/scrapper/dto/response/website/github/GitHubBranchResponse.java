package ru.tinkoff.edu.java.scrapper.dto.response.website.github;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GitHubBranchResponse {
    private String name;

    public GitHubBranch getGitHubBranch() {
        return new GitHubBranch(name);
    }
}
