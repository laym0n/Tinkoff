package ru.tinkoff.edu.java.scrapper.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class GitHubInfoResponse {
    @JsonProperty("created_at")
    private OffsetDateTime updatedAt;

    public GitHubInfoResponse() {
    }

    @Override
    public String toString() {
        return "GitHubUpdateResponse{" +
                "updatedAt=" + updatedAt +
                '}';
    }

    public GitHubInfoResponse(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
