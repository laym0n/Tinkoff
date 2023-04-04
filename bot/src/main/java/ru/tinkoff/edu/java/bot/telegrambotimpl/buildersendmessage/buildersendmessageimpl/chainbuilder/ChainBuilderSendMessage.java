package ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.buildersendmessageimpl.chainbuilder;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.BuilderSendMessage;
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
