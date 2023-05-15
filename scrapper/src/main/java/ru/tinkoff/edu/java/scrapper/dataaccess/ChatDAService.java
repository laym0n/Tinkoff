package ru.tinkoff.edu.java.scrapper.dataaccess;

import ru.tinkoff.edu.java.scrapper.entities.Chat;

public interface ChatDAService {

    void createChat(Chat newChat);

    void deleteChat(int idChat);
}
