package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class RabbitMQConfiguration {
    @Autowired
    private ApplicationConfig.RabbitMQInfo rabbitMQInfo;

    @Bean
    public RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
        return new RabbitAdmin(rabbitTemplate);
    }

    @Bean
    public DirectExchange directExchange(RabbitAdmin rabbitAdmin) {
        DirectExchange exchange = new DirectExchange(rabbitMQInfo.exchangeName(), rabbitMQInfo.exchangeDurable(),
                rabbitMQInfo.exchangeAutoDelete());
        rabbitAdmin.declareExchange(exchange);
        return exchange;
    }

    @Bean
    public Queue queue(RabbitAdmin rabbitAdmin) {
        Queue queue = QueueBuilder.durable(rabbitMQInfo.queueName())
                .withArgument("x-dead-letter-exchange", rabbitMQInfo.exchangeName() + ".dlx")
                .build();
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public Binding binding(RabbitAdmin rabbitAdmin) {
        Binding binding = BindingBuilder.bind(queue(rabbitAdmin))
            .to(directExchange(rabbitAdmin))
            .with(rabbitMQInfo.routingKey());
        rabbitAdmin.declareBinding(binding);
        return binding;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
