package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.chatdaservice;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.JDBCChatDAO;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

@AllArgsConstructor
public class JDBCChatDAService implements ChatDAService {
    private JDBCChatDAO chatDAO;

    @Override
    public void createChat(Chat entity) {
        chatDAO.add(entity);
    }

    @Override
    public void deleteChat(int idChat) {
        chatDAO.remove(idChat);
    }
}
