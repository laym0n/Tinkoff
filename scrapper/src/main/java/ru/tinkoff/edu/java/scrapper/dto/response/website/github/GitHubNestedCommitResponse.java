package ru.tinkoff.edu.java.scrapper.dto.response.website.github;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class GitHubNestedCommitResponse {
    private GitHubCommiterResponse committer;
}
