package ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

import java.time.OffsetDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class StackOverflowCommentResponse {
    @HashCodeExclude
    @JsonProperty("owner")
    private StackOverflowUserResponse owner;
    @JsonProperty("creation_date")
    @HashCodeExclude
    private  OffsetDateTime createdAt;
    @JsonProperty("comment_id")
    private  int idComment;
    public StackOverflowComment getStackOverflowComment(){
        return new StackOverflowComment(idComment);
    }
}
