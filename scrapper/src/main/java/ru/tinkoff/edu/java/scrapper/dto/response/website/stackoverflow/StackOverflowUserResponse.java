package ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class StackOverflowUserResponse {
    @JsonProperty("display_name")
    private  String name;
}
