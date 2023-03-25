package ru.tinkoff.edu.java.scrapper.dto.request;

public class RemoveLinkRequest {
    private String link;

    public RemoveLinkRequest(String link) {
        this.link = link;
    }

    public RemoveLinkRequest() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
