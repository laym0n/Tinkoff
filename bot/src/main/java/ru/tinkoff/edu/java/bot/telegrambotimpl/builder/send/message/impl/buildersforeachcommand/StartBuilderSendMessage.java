package ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.impl.buildersforeachcommand;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.Command;
import ru.tinkoff.edu.java.bot.telegrambotimpl.ParsedCommand;
import ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.BuilderSendMessage;
import ru.tinkoff.edu.java.bot.webclients.ScrapperClient;

@Component
@AllArgsConstructor
public class StartBuilderSendMessage implements BuilderSendMessage {
    private ScrapperClient scrapperClient;

    @Override
    public SendMessage getSendMessage(ParsedCommand parsedCommand) {
        if (!parsedCommand.getCommand().equals(Command.START)) {
            return null;
        }

        SendMessage result = new SendMessage();
        result.setChatId(Long.valueOf(parsedCommand.getChatId()).toString());
        try {
            scrapperClient.registryChat(parsedCommand.getChatId());
            result.setText("Ваш чат с id " + parsedCommand.getChatId() + " зарегистрирован");
        } catch (RuntimeException e) {
            result.setText(e.getMessage());
        }

        return result;
    }
}
