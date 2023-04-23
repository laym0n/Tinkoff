package ru.tinkoff.edu.java.scrapper.dto.response.website.github;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsExclude;

import java.time.OffsetDateTime;

@Data
@ToString
@EqualsAndHashCode
public class GitHubCommiterResponse {
    private OffsetDateTime date;
}
