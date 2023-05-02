package ru.tinkoff.edu.java.scrapper.dto.response.website.github;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class GitHubNestedCommitResponse {
    private GitHubCommiterResponse committer;
}
