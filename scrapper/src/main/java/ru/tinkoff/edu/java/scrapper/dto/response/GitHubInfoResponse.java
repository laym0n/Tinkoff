package ru.tinkoff.edu.java.scrapper.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GitHubInfoResponse {
    @JsonProperty("created_at")
    private OffsetDateTime updatedAt;

}
