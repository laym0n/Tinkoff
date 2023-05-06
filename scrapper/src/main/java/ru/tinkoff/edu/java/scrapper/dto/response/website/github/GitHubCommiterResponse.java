package ru.tinkoff.edu.java.scrapper.dto.response.website.github;

import lombok.*;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GitHubCommiterResponse {
    private OffsetDateTime date;
}
