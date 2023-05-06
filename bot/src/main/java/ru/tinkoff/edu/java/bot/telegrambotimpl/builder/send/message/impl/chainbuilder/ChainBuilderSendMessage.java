package ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.impl.chainbuilder;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.BuilderSendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.ParsedCommand;

@AllArgsConstructor
public class ChainBuilderSendMessage implements BuilderSendMessage{
    private BuilderSendMessage nextBuilderSendMessage;
    private BuilderSendMessage myBuilderSendMessage;
    @Override
    public SendMessage getSendMessage(ParsedCommand parsedCommand ) {
        SendMessage sendMessage = myBuilderSendMessage.getSendMessage(parsedCommand );
        if(sendMessage == null)
            sendMessage = nextBuilderSendMessage.getSendMessage(parsedCommand );
        return sendMessage;
    }
}
