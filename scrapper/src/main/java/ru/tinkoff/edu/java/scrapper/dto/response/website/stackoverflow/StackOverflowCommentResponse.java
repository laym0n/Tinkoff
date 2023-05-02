package ru.tinkoff.edu.java.scrapper.dto.response.website.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.HashCodeExclude;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.stackoverflow.StackOverflowComment;

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

    public StackOverflowComment getStackOverflowComment() {
        return new StackOverflowComment(idComment);
    }
}
