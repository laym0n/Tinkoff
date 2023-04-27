package ru.tinkoff.edu.java.bot.telegrambotimpl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.tinkoff.edu.java.bot.dto.response.LinkUpdateResponse;
import ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.BuilderSendMessage;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Component
public class LinkUpdateCheckerBot extends TelegramLongPollingBot {
    private static Logger log = Logger.getLogger(LinkUpdateCheckerBot.class.getName());
    private String botName;
    private String botToken;
    private ParserCommands parserCommands;
    private BuilderSendMessage builderSendMessage;

    public LinkUpdateCheckerBot(@Value("#{@telegramBotInfo.botName}") String botName, @Value("#{@telegramBotInfo.botToken}") String botToken,
                                ParserCommands parserCommands, @Qualifier("builderSendMessage") BuilderSendMessage builderSendMessage) {
        this.botName = botName;
        this.botToken = botToken;
        this.builderSendMessage = builderSendMessage;
        this.parserCommands = parserCommands;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Receive new Update. updateID: " + update.getUpdateId());
        ParsedCommand parsedCommand = parserCommands.getParsedCommand(update.getMessage().getText());
        parsedCommand.setChatId(update.getMessage().getChatId());
        SendMessage message = builderSendMessage.getSendMessage(parsedCommand);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException();
        }
    }
    @PostConstruct
    private void botConnect() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        createMenu();
    }
    public void createMenu(){
        List<BotCommand> botCommands = Arrays.stream(Command.values()).filter(command -> !command.equals(Command.UKNOWN))
                .map(command ->
                new BotCommand(command.getComandSyntax(), command.getComandDescription())).toList();
        try {
            this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), "ru"));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendUpdateMessages(LinkUpdateResponse request){
        for(int idChat : request.getTgChatIds()){
            try {
                this.execute(new SendMessage(Integer.toString(idChat), request.getDescription()));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
