package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.sendupdaterequeststrategy.impl;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.sendupdaterequeststrategy.SendLinkUpdateRequestStrategy;
import ru.tinkoff.edu.java.scrapper.webclients.bot.BotWebClient;

@AllArgsConstructor
@Component
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class RestAPISendLinkUpdateRequestStrategy extends SendLinkUpdateRequestStrategy {
    private BotWebClient botWebClient;
    @Override
    public void sendLinkUpdateRequest(LinkUpdateRequest linkUpdateRequest) {
        botWebClient.sendLinkUpdateRequest(linkUpdateRequest);
    }
}
