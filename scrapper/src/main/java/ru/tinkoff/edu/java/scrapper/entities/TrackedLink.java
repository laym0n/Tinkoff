package ru.tinkoff.edu.java.scrapper.entities;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import parserservice.dto.LinkInfo;

@AllArgsConstructor
@Data
public class TrackedLink {
    private int id;
    private int idChat;
    private int idWebsiteInfo;
    private LinkInfo linkInfo;

    public String getPath() {
        return linkInfo.getPath();
    }

    public URI getURI() {
        return URI.create(linkInfo.getPath());
    }

    public TrackedLink(int idChat, LinkInfo linkInfo, int idWebsiteInfo) {
        this.idChat = idChat;
        this.linkInfo = linkInfo;
        this.idWebsiteInfo = idWebsiteInfo;
    }
}
