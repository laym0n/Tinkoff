package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.ChatEntity;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAChatDAO extends JPADAO {
    private static final String NAME_OF_PARAMETER_FOR_CHAT_ID = "idChat";

    public void add(ChatEntity newChat) {
        entityManager.persist(newChat);
    }

    public void remove(int idChat) {
        entityManager.createQuery("delete from ChatEntity ch where ch.id = :idChat")
                .setParameter(NAME_OF_PARAMETER_FOR_CHAT_ID, idChat).executeUpdate();
    }

    public boolean containsChatWithId(int chatId) {
        long countRows = (Long) entityManager.createQuery("SELECT COUNT(*) FROM ChatEntity WHERE id = :idChat")
                .setParameter(NAME_OF_PARAMETER_FOR_CHAT_ID, chatId).getSingleResult();
        return (countRows != 0L);
    }

    public ChatEntity findByID(int id) {
        return entityManager
            .createQuery(
                "SELECT ch FROM ChatEntity ch WHERE ch.id = :idChat",
                ChatEntity.class
            )
            .setParameter(NAME_OF_PARAMETER_FOR_CHAT_ID, id).getSingleResult();
    }
}
