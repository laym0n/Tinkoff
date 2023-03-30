package ru.tinkoff.edu.java.scrapper.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class StackOverflowInfoResponse {
    @JsonProperty("last_edit_date")
    private OffsetDateTime lastEditDate;

    public StackOverflowInfoResponse(OffsetDateTime lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    @Override
    public String toString() {
        return "StackOverflowInfoResponse{" +
                "lastEditDate=" + lastEditDate +
                '}';
    }

    public StackOverflowInfoResponse() {
    }

    public OffsetDateTime getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(OffsetDateTime lastEditDate) {
        this.lastEditDate = lastEditDate;
    }
}
