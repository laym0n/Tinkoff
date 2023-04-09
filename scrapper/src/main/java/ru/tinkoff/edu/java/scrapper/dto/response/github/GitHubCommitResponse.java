package ru.tinkoff.edu.java.scrapper.dto.response.github;

public record GitHubCommitResponse(String sha, GitHubNestedCommitResponse commit) {
}
