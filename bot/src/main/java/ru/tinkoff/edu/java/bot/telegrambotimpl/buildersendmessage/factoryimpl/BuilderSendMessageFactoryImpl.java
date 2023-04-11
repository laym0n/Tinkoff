package ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.factoryimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.BuilderSendMessage;
import ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.BuilderSendMessageFactory;
import ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.buildersendmessageimpl.buildersforeachcommand.*;
import ru.tinkoff.edu.java.bot.telegrambotimpl.buildersendmessage.buildersendmessageimpl.chainbuilder.ChainBuilderSendMessage;

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
