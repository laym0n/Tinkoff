package ru.tinkoff.edu.java.bot.dto;

public class LinkUpdateRequest {
    private int id;
    private String url;
    private String description;
    private int[] tgChatIds;

    public LinkUpdateRequest() {
    }

    public LinkUpdateRequest(int id, String url, String description, int[] tgChatIds) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.tgChatIds = tgChatIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int[] getTgChatIds() {
        return tgChatIds;
    }

    public void setTgChatIds(int[] tgChatIds) {
        this.tgChatIds = tgChatIds;
    }
}
