package ru.tinkoff.edu.java.bot.usecases.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.response.LinkUpdateResponse;
import ru.tinkoff.edu.java.bot.telegrambotimpl.LinkUpdateCheckerBot;
import ru.tinkoff.edu.java.bot.usecases.SendUpdateLinkUseCase;

@AllArgsConstructor
@Service
public class SendUpdateLinkUseCaseImpl implements SendUpdateLinkUseCase {
    private LinkUpdateCheckerBot myBot;
    @Override
    public void sendUpdateLink(LinkUpdateResponse linkUpdateResponse) {
        myBot.sendUpdateMessages(linkUpdateResponse);
    }
}
