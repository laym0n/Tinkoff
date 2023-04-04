package ru.tinkoff.edu.java.bot.webclients;


import ru.tinkoff.edu.java.bot.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.bot.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.response.ListLinksResponse;

public interface ScrapperClient {
    LinkResponse addLink(long idChat, AddLinkRequest addLinkRequest);
    LinkResponse removeLink(long idChat, RemoveLinkRequest removeLinkRequest);
    ListLinksResponse allLinksFromChat(long idChat);
    void registryChat(long idChat);
    void removeChat(long idChat);
}
