package ru.tinkoff.edu.java.scrapper.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StackoverflowResponse {
    @JsonProperty("items")
    private StackOverflowInfoResponse[] items;

    public StackoverflowResponse() {
    }

    public StackoverflowResponse(StackOverflowInfoResponse[] items) {
        this.items = items;
    }

    public StackOverflowInfoResponse[] getItems() {
        return items;
    }

    public void setItems(StackOverflowInfoResponse[] items) {
        this.items = items;
    }
}
