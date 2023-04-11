package ru.tinkoff.edu.java.scrapper.usecases.impl.managechats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import ru.tinkoff.edu.java.scrapper.usecases.ManageChatsUseCase;

import java.security.InvalidParameterException;

@Component
public class ManageChatsUseCaseImpl implements ManageChatsUseCase {
    private ChatDAService chatDAService;
    @Autowired
    public ManageChatsUseCaseImpl(ChatDAService chatDAService) {
        this.chatDAService = chatDAService;
    }

    @Override
    public void registryChat(int idChat) {
        if(chatDAService.containsChatWithId(idChat))
            throw new InvalidParameterException("Chat with id " + idChat + " is already registered");

        Chat newChat = new Chat(idChat);
        chatDAService.create(newChat);
    }

    @Override
    public void removeChat(int idChat) {
        if(!chatDAService.containsChatWithId(idChat))
            throw new NotFoundException("Chat with id " + idChat + " is not registered");

        chatDAService.delete(idChat);
    }
}
