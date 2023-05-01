package ru.tinkoff.edu.java.bot.usecases;

import ru.tinkoff.edu.java.bot.dto.response.LinkUpdateResponse;

public interface SendUpdateLinkUseCase {
    void sendUpdateLink(LinkUpdateResponse linkUpdateResponse);
}
