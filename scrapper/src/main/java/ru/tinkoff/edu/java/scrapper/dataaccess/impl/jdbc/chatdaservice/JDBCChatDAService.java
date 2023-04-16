package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.chatdaservice;

import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

import java.util.Optional;

public class JDBCChatDAService implements ChatDAService {
    @Override
    public Chat create(Chat entity) {

        return null;
    }

    @Override
    public Optional<Chat> findById(Integer idEntity) {
        return Optional.empty();
    }

    @Override
    public void update(Chat entity) {

    }

    @Override
    public void delete(Integer idEntity) {

    }

    @Override
    public boolean containsChatWithId(int chatId) {
        return false;
    }
}
