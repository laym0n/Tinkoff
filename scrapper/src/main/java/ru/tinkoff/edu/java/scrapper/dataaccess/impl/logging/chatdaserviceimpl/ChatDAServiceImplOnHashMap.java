package ru.tinkoff.edu.java.scrapper.dataaccess.impl.logging.chatdaserviceimpl;

import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Optional;

public class ChatDAServiceImplOnHashMap implements ChatDAService {
    HashMap<Integer, Chat> savedChats = new HashMap<>();
    @Override
    public Chat create(Chat entity) {
        if(savedChats.containsKey(entity.getId()))
            throw new InvalidParameterException("Chat with id  " + entity.getId()+ " already exist in database");
        savedChats.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Chat> findById(Integer idEntity) {
        Optional<Chat> result = Optional.ofNullable(savedChats.get(idEntity));
        return result;
    }

    @Override
    public void update(Chat entity) {
        savedChats.put(entity.getId(), entity);
    }

    @Override
    public void delete(Integer idEntity) {
        savedChats.remove(idEntity);
    }

    @Override
    public boolean containsChatWithId(int chatId) {
        return savedChats.containsKey(chatId);
    }

}
