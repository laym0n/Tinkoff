package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.chatdaservice;

import jakarta.persistence.EntityExistsException;
import java.security.InvalidParameterException;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAChatDAO;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.ChatEntity;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

@AllArgsConstructor
@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAChatDAService implements ChatDAService {
    private JPAChatDAO chatDAO;

    @Override
    public void createChat(Chat chat) {
        ChatEntity chatEntity = new ChatEntity(chat);
        try {
            chatDAO.add(chatEntity);
        } catch (EntityExistsException ex) {
            throw new InvalidParameterException("Chat with id "
                + chat.getId() + " already registered");
        }
    }

    @Override
    public void deleteChat(int idChat) {
        chatDAO.remove(idChat);
    }
}
