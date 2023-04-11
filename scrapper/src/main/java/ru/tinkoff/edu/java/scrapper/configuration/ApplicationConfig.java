package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import parserservice.ParserLinks;
import parserservice.factories.ParserLinksFactory;
import parserservice.factories.factoryimpl.ParserLinksFactoryImpl;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String test, @Bean Scheduler scheduler, @Bean BotInfo botInfo) {
    record Scheduler(Duration interval){

    }
    record BotInfo(String path){

    }
}
