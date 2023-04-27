package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.BuilderSendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.BuilderSendMessageFactory;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String test, @Bean TelegramBotInfo telegramBotInfo,
                                @Bean ScrapperInfo scrapperInfo) {

    record TelegramBotInfo(String botName, String botToken){

    }
    record ScrapperInfo(String pathForRequests){

    }
    @Bean
    public BuilderSendMessage builderSendMessage(BuilderSendMessageFactory factory){
        return factory.getBuilderSendMessage();
    }
}
