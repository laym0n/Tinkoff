package ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowAnswer;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class StackOverflowAnswerResponse {
    @HashCodeExclude
    @JsonProperty("owner")
    private StackOverflowUserResponse owner;
    @JsonProperty("creation_date")
    @HashCodeExclude
    private  OffsetDateTime creationDate;
    @JsonProperty("last_edit_date")
    @HashCodeExclude
    private  OffsetDateTime lastEditDate;
    @JsonProperty("answer_id")
    private  int answerId;
    public StackOverflowAnswer getStackOverflowAnswer(){
        return new StackOverflowAnswer(answerId, owner.getName(), (lastEditDate != null ? lastEditDate : creationDate));
    }
    public OffsetDateTime getLastEditDate(){
        if(lastEditDate == null)
            lastEditDate = creationDate;
        return lastEditDate;
    }
}
