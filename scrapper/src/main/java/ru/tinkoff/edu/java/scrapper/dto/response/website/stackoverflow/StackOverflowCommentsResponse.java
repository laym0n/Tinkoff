package ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class StackOverflowCommentsResponse {
    @JsonProperty("items")
    private StackOverflowCommentResponse[] items;
}
