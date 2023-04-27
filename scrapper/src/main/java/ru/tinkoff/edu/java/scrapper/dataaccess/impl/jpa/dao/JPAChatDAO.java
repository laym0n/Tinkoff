package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.ChatEntity;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

import javax.sql.DataSource;

@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JPAChatDAO extends JPADAO {
    public void add(ChatEntity newChat){
        entityManager.persist(newChat);
    }
    public void remove(int idChat){
        entityManager.createQuery("delete from ChatEntity ch where ch.id = :idChat")
                .setParameter("idChat", idChat).executeUpdate();
    }
    public boolean containsChatWithId(int chatId){
        long countRows = (Long) entityManager.createQuery("SELECT COUNT(*) FROM ChatEntity WHERE id = :idChat")
                .setParameter("idChat", chatId).getSingleResult();
        return (countRows != 0l);
    }
    public ChatEntity findByID(int id){
        return entityManager.createQuery(
                "SELECT ch FROM ChatEntity ch WHERE ch.id = :idChat", ChatEntity.class
        ).setParameter("idChat", id).getSingleResult();
    }
}
