package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
@AllArgsConstructor
public class RabbitMQConfiguration {
    private ApplicationConfig.RabbitMQInfo rabbitMQInfo;
    @Bean
    public RabbitAdmin rabbitAdmin(RabbitTemplate connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
    @Bean
    public DirectExchange directExchange(RabbitAdmin rabbitAdmin){
        DirectExchange exchange = new DirectExchange(rabbitMQInfo.exchangeName(), rabbitMQInfo.exchangeDurable(),
                rabbitMQInfo.exchangeAutoDelete());
        rabbitAdmin.declareExchange(exchange);
        return exchange;
    }
    @Bean
    public Queue queue(RabbitAdmin rabbitAdmin){
        Queue queue = QueueBuilder.durable(rabbitMQInfo.queueName())
                .withArgument("x-dead-letter-exchange", rabbitMQInfo.exchangeName() + ".dlx")
                .build();
        rabbitAdmin.declareQueue(queue);
        return queue;
    }
    @Bean
    public Binding binding(RabbitAdmin rabbitAdmin){
        Binding binding = BindingBuilder.bind(queue(rabbitAdmin)).to(directExchange(rabbitAdmin)).with(rabbitMQInfo.routingKey());
        rabbitAdmin.declareBinding(binding);
        return binding;
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
