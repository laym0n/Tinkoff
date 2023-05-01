package ru.tinkoff.edu.java.scrapper.usecases.impl.checkupdatelinks.sendupdaterequeststrategy;

import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

public abstract class SendLinkUpdateRequestStrategy {
    public abstract void sendLinkUpdateRequest(LinkUpdateRequest linkUpdateRequest);
}
