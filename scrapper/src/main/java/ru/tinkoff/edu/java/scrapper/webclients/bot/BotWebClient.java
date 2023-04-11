package ru.tinkoff.edu.java.scrapper.webclients.bot;

import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

public interface BotWebClient {
    void sendLinkUpdateRequest(LinkUpdateRequest request);
}
