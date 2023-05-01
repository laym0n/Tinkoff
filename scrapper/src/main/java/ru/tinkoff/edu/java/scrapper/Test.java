package ru.tinkoff.edu.java.scrapper;


import dto.LinkUpdateDTO;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.sendupdaterequeststrategy.impl.RabbitMQSendLinkUpdateRequestStrategy;

import java.net.URI;

public class Test {
    public static void main(String[] args){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localhost");
        cachingConnectionFactory.setUsername("guest");
        cachingConnectionFactory.setPassword("guest");
        cachingConnectionFactory.setPort(5672);

        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        rabbitTemplate.setExchange("testExchange");
        RabbitMQSendLinkUpdateRequestStrategy strategy = new RabbitMQSendLinkUpdateRequestStrategy(rabbitTemplate);
        LinkUpdateDTO sdfsd = new LinkUpdateDTO(
                1,
                URI.create("http://localhost:15672/#/queues/%2F/testQueue"),
                "asdsad",
                new int[] {1444737395}
        );
        rabbitTemplate.convertAndSend("LinkUpdateRequest", sdfsd);
        rabbitTemplate.convertAndSend("LinkUpdateRequest", "asfsdfsdfsdfsdf");
        System.out.println("342342423");
    }
}
