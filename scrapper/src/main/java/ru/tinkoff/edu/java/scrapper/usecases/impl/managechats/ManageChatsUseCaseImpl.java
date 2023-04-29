package ru.tinkoff.edu.java.scrapper.usecases.impl.managechats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import ru.tinkoff.edu.java.scrapper.usecases.ManageChatsUseCase;

@Component
public class ManageChatsUseCaseImpl implements ManageChatsUseCase {
    private ChatDAService chatDAService;
    @Autowired
    public ManageChatsUseCaseImpl(ChatDAService chatDAService) {
        this.chatDAService = chatDAService;
    }

    @Override
    public void registryChat(int idChat) {
        Chat newChat = new Chat(idChat);
        chatDAService.createChat(newChat);
    }

    @Override
    public void removeChat(int idChat) {
        chatDAService.deleteChat(idChat);
    }
}
