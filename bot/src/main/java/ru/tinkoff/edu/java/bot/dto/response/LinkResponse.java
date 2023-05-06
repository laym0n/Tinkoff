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
public class LinkResponse {
    private int id;
    private URI uri;
    private String description;
    private String code;
    private String exceptionName;
    private String exceptionMessage;
    private String[] stacktrace;
}
