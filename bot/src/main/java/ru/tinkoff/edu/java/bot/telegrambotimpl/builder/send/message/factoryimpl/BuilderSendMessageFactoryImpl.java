package ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.factoryimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.BuilderSendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.BuilderSendMessageFactory;
import ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.impl.buildersforeachcommand.*;
import ru.tinkoff.edu.java.bot.telegrambotimpl.builder.send.message.impl.chainbuilder.ChainBuilderSendMessage;

@Component
@AllArgsConstructor
public class BuilderSendMessageFactoryImpl implements BuilderSendMessageFactory {
    private UnknownBuilderSendMessage unknownBuilderSendMessage;
    private UntrackBuilderSendMessage untrackBuilderSendMessage;
    private TrackBuilderSendMessage trackBuilderSendMessage;
    private StartBuilderSendMessage startBuilderSendMessage;
    private ListBuilderSendMessage listBuilderSendMessage;
    private HelpBuilderSendMessage helpBuilderSendMessage;
    @Override
    public BuilderSendMessage getBuilderSendMessage() {
        ChainBuilderSendMessage firstChain = new ChainBuilderSendMessage(unknownBuilderSendMessage, untrackBuilderSendMessage);
        ChainBuilderSendMessage secondChain = new ChainBuilderSendMessage(firstChain, trackBuilderSendMessage);
        ChainBuilderSendMessage thirdChain = new ChainBuilderSendMessage(secondChain, startBuilderSendMessage);
        ChainBuilderSendMessage fourChain = new ChainBuilderSendMessage(thirdChain, listBuilderSendMessage);
        ChainBuilderSendMessage fiveChain = new ChainBuilderSendMessage(fourChain, helpBuilderSendMessage);
        return fiveChain;
    }
}
