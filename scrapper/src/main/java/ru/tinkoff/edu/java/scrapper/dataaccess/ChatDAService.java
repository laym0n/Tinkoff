package ru.tinkoff.edu.java.scrapper.dataaccess;

import ru.tinkoff.edu.java.scrapper.entities.Chat;

public interface ChatDAService extends CRUDService<Chat, Integer>{
    boolean containsChatWithId(int chatId);
}
