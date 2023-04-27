package ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.impl.buildersforeachcommand;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.BuilderSendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.ParsedCommand;

@Component
@AllArgsConstructor
public class UnknownBuilderSendMessage implements BuilderSendMessage {
    @Override
    public SendMessage getSendMessage(ParsedCommand parsedCommand ) {
        SendMessage result = new SendMessage(Long.valueOf(parsedCommand.getChatId()).toString(),
                "Команда " + parsedCommand.getText() + " не распознана.");
        return result;
    }
}
