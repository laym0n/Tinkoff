package ru.tinkoff.edu.java.scrapper.dto.request;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LinkUpdateRequest {
    private int id;
    private URI uri;
    private String description;
    private int[] tgChatIds;

}
