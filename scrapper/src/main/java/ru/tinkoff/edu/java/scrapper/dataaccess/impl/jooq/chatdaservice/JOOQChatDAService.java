package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.chatdaservice;

import java.security.InvalidParameterException;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao.JOOQChatDAO;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

@AllArgsConstructor
@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQChatDAService implements ChatDAService {
    private JOOQChatDAO chatDAO;

    @Override
    public void createChat(Chat newChat) {
        try {
            chatDAO.add(newChat);
        } catch (DuplicateKeyException ex) {
            throw new InvalidParameterException("Chat with id"
                + newChat.getId() + " already registered");
        }
    }

    @Override
    public void deleteChat(int idChat) {
        chatDAO.remove(idChat);
    }
}
