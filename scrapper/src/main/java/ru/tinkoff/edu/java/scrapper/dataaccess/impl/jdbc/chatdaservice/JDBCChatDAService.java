package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.chatdaservice;

import java.security.InvalidParameterException;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCChatDAO;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

@AllArgsConstructor
@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JDBCChatDAService implements ChatDAService {
    private JDBCChatDAO chatDAO;

    @Override
    public void createChat(Chat entity) {
        try {
            chatDAO.add(entity);
        } catch (DuplicateKeyException ex) {
            throw new InvalidParameterException("Chat with id"
                + entity.getId() + " already registered");
        }
    }

    @Override
    public void deleteChat(int idChat) {
        chatDAO.remove(idChat);
    }
}
