package ru.tinkoff.edu.java.bot.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListLinksResponse {
    private int size;
    private LinkResponse[] links;
    private String description;
    private String code;
    private String exceptionName;
    private String exceptionMessage;
    private String[] stacktrace;

}
