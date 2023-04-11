package ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.buildersendmessageimpl.buildersforeachcommand;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.webjars.NotFoundException;
import parserservice.ParserLinks;
import parserservice.dto.WebsiteInfo;
import ru.tinkoff.edu.java.bot.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.BuilderSendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.Command;
import ru.tinkoff.edu.java.bot.telegrambotimpl.ParsedCommand;
import ru.tinkoff.edu.java.bot.webclients.ScrapperClient;

import java.security.InvalidParameterException;

@Component
@AllArgsConstructor
public class UntrackBuilderSendMessage implements BuilderSendMessage {
    private ScrapperClient scrapperClient;
    @Override
    public SendMessage getSendMessage(ParsedCommand parsedCommand ) {
        if(!parsedCommand.getCommand().equals(Command.UNTRACK))
            return null;

        SendMessage result = new SendMessage();
        result.setChatId(Long.valueOf(parsedCommand.getChatId()).toString());

        try{
            RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(parsedCommand.getText());
            scrapperClient.removeLink(parsedCommand.getChatId(), removeLinkRequest);
            result.setText("Ссылка " + parsedCommand.getText() + " больше не отслеживается.");
        }
        catch (RuntimeException e){
            result.setText(e.getMessage());
        }

        return result;
    }
}
