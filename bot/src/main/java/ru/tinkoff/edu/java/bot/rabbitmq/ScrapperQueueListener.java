package ru.tinkoff.edu.java.bot.rabbitmq;

import dto.LinkUpdateDTO;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.dto.response.LinkUpdateResponse;
import ru.tinkoff.edu.java.bot.usecases.SendUpdateLinkUseCase;

@Component
@AllArgsConstructor
@RabbitListener(queues = "${app.rabbitMQInfo.queueName}")
public class ScrapperQueueListener {
    private SendUpdateLinkUseCase sendUpdateLinkUseCase;
    @RabbitHandler
    public void receiver(LinkUpdateDTO update) {
        LinkUpdateResponse response = new LinkUpdateResponse(
                update.getId(),
                update.getUri(),
                update.getDescription(),
                update.getTgChatIds()
        );
        sendUpdateLinkUseCase.sendUpdateLink(response);
    }
}
