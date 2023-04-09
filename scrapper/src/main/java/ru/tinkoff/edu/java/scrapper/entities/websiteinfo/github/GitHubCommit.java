package ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github;

import java.time.OffsetDateTime;

public record GitHubCommit(String sha, OffsetDateTime dateTimeOfCommit) {
}
