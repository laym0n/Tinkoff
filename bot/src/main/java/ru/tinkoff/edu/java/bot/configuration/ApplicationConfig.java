package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import parserservice.ParserLinks;
import parserservice.chainresponsibilityparser.ParserLinksImpl;
import parserservice.chainresponsibilityparser.parserstrategies.GitHubParserStrategy;
import parserservice.chainresponsibilityparser.parserstrategies.StackOverflowParserStrategy;
import parserservice.factories.ParserLinksFactory;
import parserservice.factories.factoryimpl.ParserLinksFactoryImpl;
import ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.BuilderSendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.BuilderSendMessageFactory;
import ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.buildersendmessageimpl.buildersforeachcommand.*;
import ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.buildersendmessageimpl.chainbuilder.ChainBuilderSendMessage;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String test, @Bean Scheduler scheduler, @Bean TelegramBotInfo telegramBotInfo,
                                @Bean ScrapperInfo scrapperInfo) {
    record Scheduler(Duration interval){

    }
    record TelegramBotInfo(String botName, String botToken){

    }
    record ScrapperInfo(String pathForRequests){

    }
    @Bean
    public BuilderSendMessage builderSendMessage(BuilderSendMessageFactory factory){
        return factory.getBuilderSendMessage();
    }
}
