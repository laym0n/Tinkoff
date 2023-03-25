package ru.tinkoff.edu.java.scrapper.dto.response;

public class ListLinksResponse {
    private int size;
    private LinkResponse[] links;

    public ListLinksResponse() {
    }

    public ListLinksResponse(int size, LinkResponse[] links) {
        this.size = size;
        this.links = links;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public LinkResponse[] getLinks() {
        return links;
    }

    public void setLinks(LinkResponse[] links) {
        this.links = links;
    }
}
