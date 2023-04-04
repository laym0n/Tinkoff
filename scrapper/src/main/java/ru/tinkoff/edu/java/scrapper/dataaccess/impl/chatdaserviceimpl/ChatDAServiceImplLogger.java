package ru.tinkoff.edu.java.scrapper.dataaccess.impl.chatdaserviceimpl;

import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

import java.util.Optional;
import java.util.logging.Logger;

@Repository
public class ChatDAServiceImplLogger implements ChatDAService {
    private static Logger log = Logger.getLogger(ChatDAServiceImplLogger.class.getName());
    @Override
    public Chat create(Chat entity) {
        log.info("Create new chat with id " + entity.getId());
        return entity;
    }

    @Override
    public Optional<Chat> findById(Integer idEntity) {
        log.info("Find chat with id " + idEntity);
        return Optional.of(new Chat(idEntity));
    }

    @Override
    public void update(Chat entity) {
        log.info("Update chat with id " + entity.getId());
    }

    @Override
    public void delete(Integer idEntity) {
        log.info("Delete chat with id " + idEntity);
    }

    @Override
    public boolean containsChatWithId(int chatId) {
        log.info("Check contains chat with id " + chatId);
        return true;
    }
}
