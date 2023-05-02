package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.sendupdaterequeststrategy.impl;

import dto.LinkUpdateDTO;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.sendupdaterequeststrategy.SendLinkUpdateRequestStrategy;

@AllArgsConstructor
@Component
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class RabbitMQSendLinkUpdateRequestStrategy extends SendLinkUpdateRequestStrategy {
    private static @Value("#{rabbitMQInfo.routingKey}") String routingKey;
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendLinkUpdateRequest(LinkUpdateRequest linkUpdateRequest) {
        LinkUpdateDTO dto = new LinkUpdateDTO(
                linkUpdateRequest.getId(),
                linkUpdateRequest.getUri(),
                linkUpdateRequest.getDescription(),
                linkUpdateRequest.getTgChatIds()
        );
        rabbitTemplate.convertAndSend(routingKey, dto);
    }
}
