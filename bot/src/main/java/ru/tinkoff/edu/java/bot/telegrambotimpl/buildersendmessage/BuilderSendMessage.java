package ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.ParsedCommand;

public interface BuilderSendMessage {
    SendMessage getSendMessage(ParsedCommand parsedCommand );
}
