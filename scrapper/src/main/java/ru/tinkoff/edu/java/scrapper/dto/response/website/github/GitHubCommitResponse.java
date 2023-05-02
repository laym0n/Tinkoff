package ru.tinkoff.edu.java.scrapper.dto.response.website.github;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.HashCodeExclude;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class GitHubCommitResponse {
    private String sha;
    @HashCodeExclude
    private GitHubNestedCommitResponse commit;

    public GitHubCommit getGitHubCommit() {
        return new GitHubCommit(sha);
    }
}
