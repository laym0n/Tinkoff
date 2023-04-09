package ru.tinkoff.edu.java.scrapper.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import parserservice.dto.LinkInfo;

import java.net.URI;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class TrackedLink {
    private int id;
    private int idChat;
    private LinkInfo linkInfo;
    public String getPath(){
        return linkInfo.getPath();
    }
    public URI getURI(){
        return URI.create(linkInfo.getPath());
    }

    public TrackedLink(int idChat, LinkInfo linkInfo) {
        this.idChat = idChat;
        this.linkInfo = linkInfo;
    }
}
