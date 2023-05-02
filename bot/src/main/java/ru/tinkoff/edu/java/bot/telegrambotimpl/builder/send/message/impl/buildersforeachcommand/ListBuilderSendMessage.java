package ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.impl.buildersforeachcommand;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.tinkoff.edu.java.bot.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.bot.telegrambotimpl.Command;
import ru.tinkoff.edu.java.bot.telegrambotimpl.ParsedCommand;
import ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.BuilderSendMessage;
import ru.tinkoff.edu.java.bot.webclients.ScrapperClient;

@Component
@AllArgsConstructor
public class ListBuilderSendMessage implements BuilderSendMessage {
    private ScrapperClient scrapperClient;

    @Override
    public SendMessage getSendMessage(ParsedCommand parsedCommand) {
        if (!parsedCommand.getCommand().equals(Command.LIST)) {
            return null;
        }

        SendMessage result = new SendMessage();
        result.setChatId(Long.toString(parsedCommand.getChatId()));

        try {
            ListLinksResponse listLinksResponse = scrapperClient.allLinksFromChat(parsedCommand.getChatId());
            if (listLinksResponse.getLinks().length == 0) {
                result.setText("Ни одной ссылки ещё не отслеживается");
            } else {
                StringBuilder stringBuilder = new StringBuilder("Отслеживаемые ссылки:\n");
                Arrays.stream(listLinksResponse.getLinks())
                    .forEach(linkResponse -> stringBuilder.append(linkResponse.getUri()));
                result.setText(stringBuilder.toString());
            }
        } catch (RuntimeException e) {
            result.setText(e.getMessage());
        }

        return result;
    }
}
