package ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.buildersendmessageimpl.buildersforeachcommand;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.BuilderSendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.Command;
import ru.tinkoff.edu.java.bot.telegrambotimpl.ParsedCommand;

import java.util.Arrays;

@Component
public class HelpBuilderSendMessage implements BuilderSendMessage {
    private String description;
    public HelpBuilderSendMessage() {
        StringBuilder stringBuilder = new StringBuilder("Этот бот умеет отслеживать обновления сайтов по ссылкам.\n");
        Arrays.stream(Command.values()).forEach(command -> stringBuilder.append(command.description));
        description = stringBuilder.toString();
    }

    @Override
    public SendMessage getSendMessage(ParsedCommand parsedCommand ) {
        if(!parsedCommand.getCommand().equals(Command.HELP))
            return null;

        SendMessage result = new SendMessage();
        result.setChatId(Long.valueOf(parsedCommand.getChatId()).toString());
        result.setText(description);

        return result;
    }
}
