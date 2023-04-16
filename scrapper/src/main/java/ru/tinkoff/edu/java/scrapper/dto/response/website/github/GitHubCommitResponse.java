package ru.tinkoff.edu.java.scrapper.dto.response.website.github;

import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class GitHubCommitResponse {
    private String sha;
    @HashCodeExclude
    private GitHubNestedCommitResponse commit;
    public GitHubCommit getGitHubCommit(){
        return new GitHubCommit(sha);
    }
}
