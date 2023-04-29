package ru.tinkoff.edu.java.scrapper.dataaccess.impl.logging.chatdaserviceimpl;

import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

import java.text.MessageFormat;
import java.util.logging.Logger;

public class ChatDAServiceImplLogger implements ChatDAService {
    private static Logger log = Logger.getLogger(ChatDAServiceImplLogger.class.getName());
    @Override
    public void createChat(Chat entity) {
        log.info("Create new chat with id " + entity.getId());
    }
    @Override
    public void deleteChat(int idEntity) {
        log.info(MessageFormat.format("Delete chat with id {0}", idEntity));
    }
}
