package ru.tinkoff.edu.java.bot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.net.URI;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LinkUpdateResponse {
    private int id;
    private URI uri;
    private String description;
    private int[] tgChatIds;

}
