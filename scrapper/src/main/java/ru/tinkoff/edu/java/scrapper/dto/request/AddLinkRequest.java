package ru.tinkoff.edu.java.scrapper.dto.request;

public class AddLinkRequest {
    private String link;

    public AddLinkRequest(String link) {
        this.link = link;
    }

    public AddLinkRequest() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
