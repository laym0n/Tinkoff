package ru.tinkoff.edu.java.scrapper.dto.response.stackoverflow;

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
public class StackOverflowQuestionResponse {
    @JsonProperty("last_edit_date")
    private OffsetDateTime lastEditDate;

}
