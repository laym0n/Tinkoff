package ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.buildersendmessageimpl.buildersforeachcommand;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import parserservice.ParserLinks;
import parserservice.dto.WebsiteInfo;
import ru.tinkoff.edu.java.bot.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.BuilderSendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.Command;
import ru.tinkoff.edu.java.bot.telegrambotimpl.ParsedCommand;
import ru.tinkoff.edu.java.bot.webclients.ScrapperClient;

import java.security.InvalidParameterException;

@Component
@AllArgsConstructor
public class TrackBuilderSendMessage implements BuilderSendMessage {
    private ScrapperClient scrapperClient;
    @Override
    public SendMessage getSendMessage(ParsedCommand parsedCommand ) {
        if(!parsedCommand.getCommand().equals(Command.TRACK))
            return null;

        SendMessage result = new SendMessage();
        result.setChatId(Long.valueOf(parsedCommand.getChatId()).toString());

        try{
            AddLinkRequest addLinkRequest = new AddLinkRequest(parsedCommand.getText());
            LinkResponse linkResponse = scrapperClient.addLink(parsedCommand.getChatId(), addLinkRequest);
            result.setText("Ссылка " + parsedCommand.getText() + " успешно отслеживается");
        }
        catch (RuntimeException e){
            result.setText(e.getMessage());
        }

        return result;
    }
}
