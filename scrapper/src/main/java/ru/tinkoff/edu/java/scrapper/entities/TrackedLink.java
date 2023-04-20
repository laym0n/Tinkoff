package ru.tinkoff.edu.java.scrapper.entities;

import lombok.*;
import parserservice.dto.LinkInfo;

import java.net.URI;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class TrackedLink {
    private int id;
    private int idChat;
    private int idWebsiteInfo;
    private LinkInfo linkInfo;
    public String getPath(){
        return linkInfo.getPath();
    }
    public URI getURI(){
        return URI.create(linkInfo.getPath());
    }

    public TrackedLink(int idChat, LinkInfo linkInfo, int idWebsiteInfo) {
        this.idChat = idChat;
        this.linkInfo = linkInfo;
        this.idWebsiteInfo = idWebsiteInfo;
    }
}
