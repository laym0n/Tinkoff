package ru.tinkoff.edu.java.scrapper.dto.response.website.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
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
public class GitHubInfoResponse {
    @JsonProperty("created_at")
    private OffsetDateTime updatedAt;
}
