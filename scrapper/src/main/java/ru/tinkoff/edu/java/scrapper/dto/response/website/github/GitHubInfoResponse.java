package ru.tinkoff.edu.java.scrapper.dto.response.website.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class GitHubInfoResponse {
    @JsonProperty("created_at")
    private OffsetDateTime updatedAt;
}
