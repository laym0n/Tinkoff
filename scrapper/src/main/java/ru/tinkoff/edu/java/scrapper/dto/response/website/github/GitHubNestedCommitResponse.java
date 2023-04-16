package ru.tinkoff.edu.java.scrapper.dto.response.website.github;

import lombok.*;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class GitHubNestedCommitResponse {
    private GitHubCommiterResponse committer;
}
