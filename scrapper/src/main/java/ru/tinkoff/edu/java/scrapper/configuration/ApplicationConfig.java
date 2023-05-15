package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = true)
public record ApplicationConfig(@NotNull String test,
                                @Bean Scheduler scheduler,
                                @Bean BotInfo botInfo,
                                @Bean RabbitMQInfo rabbitMQInfo) {
    record Scheduler(Duration interval) {

    }

    record BotInfo(String path) {

    }

    public record RabbitMQInfo(String queueName,
                        boolean queueDurable,
                        String exchangeName,
                        boolean exchangeDurable,
                        boolean exchangeAutoDelete,
                        String routingKey) {

    }
}
