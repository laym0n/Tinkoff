package ru.tinkoff.edu.java.bot.configuration;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties
@AllArgsConstructor
public class RabbitMQConfiguration {
    @Autowired
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
    public Binding binding(RabbitAdmin rabbitAdmin, Queue queue, DirectExchange directExchange){
        Binding binding = BindingBuilder.bind(queue).to(directExchange).with(rabbitMQInfo.routingKey());
        rabbitAdmin.declareBinding(binding);
        return binding;
    }
    @Bean
    public Queue deadLetterQueue(RabbitAdmin rabbitAdmin){
        Queue queue = QueueBuilder.durable(rabbitMQInfo.queueName() + ".dlq")
                .build();
        rabbitAdmin.declareQueue(queue);
        return queue;
    }
    @Bean
    public FanoutExchange deadLetterExchange(RabbitAdmin rabbitAdmin){
        FanoutExchange exchange = new FanoutExchange(rabbitMQInfo.exchangeName() + ".dlx", rabbitMQInfo.exchangeDurable(),
                rabbitMQInfo.exchangeAutoDelete());
        rabbitAdmin.declareExchange(exchange);
        return exchange;
    }
    @Bean
    public Binding deadLetterBinding(RabbitAdmin rabbitAdmin, Queue deadLetterQueue,
                                     FanoutExchange fanoutExchange){
        Binding binding = BindingBuilder.bind(deadLetterQueue)
                .to(fanoutExchange);
        rabbitAdmin.declareBinding(binding);
        return binding;
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
