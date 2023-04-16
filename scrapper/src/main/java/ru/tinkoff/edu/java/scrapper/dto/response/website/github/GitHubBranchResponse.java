package ru.tinkoff.edu.java.scrapper.dto.response.website.github;

import lombok.*;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class GitHubBranchResponse {
    private String name;
    public GitHubBranch getGitHubBranch(){
        return new GitHubBranch(name);
    }

}
