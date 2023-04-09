package ru.tinkoff.edu.java.scrapper.dto.response.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowUserResponse(@JsonProperty("display_name") String name) {
}
