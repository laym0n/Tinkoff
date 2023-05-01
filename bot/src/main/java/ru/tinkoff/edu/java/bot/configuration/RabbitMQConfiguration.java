package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "rabbit")
@Configuration
public class RabbitMQConfiguration {
    @Autowired
    private ApplicationConfig.RabbitMQInfo rabbitMQInfo;
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
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
