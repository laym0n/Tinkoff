package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.chatdaservice;

import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao.JPAChatDAO;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

import java.security.InvalidParameterException;

@AllArgsConstructor
public class JPAChatDAService implements ChatDAService {
    private JPAChatDAO chatDAO;

    @Override
    public void createChat(Chat entity) {
        try {
            chatDAO.add(entity);
        }
        catch (DuplicateKeyException ex){
            throw new InvalidParameterException("Chat with id" +
                    entity.getId() + " already registered");
        }
    }
    @Override
    public void deleteChat(int idChat) {
        chatDAO.remove(idChat);
    }
}
