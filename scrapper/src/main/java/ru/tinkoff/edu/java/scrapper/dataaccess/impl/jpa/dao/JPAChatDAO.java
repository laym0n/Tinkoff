package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.ChatEntity;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

import javax.sql.DataSource;

public class JPAChatDAO extends JPADAO {
    public void add(ChatEntity newChat){
        entityManager.persist(newChat);
    }
    public void remove(int idChat){
        entityManager.createQuery("delete from ChatEntity ch where ch.id = ?").executeUpdate();
    }
    public boolean containsChatWithId(int chatId){
        int countRows = entityManager.createQuery("SELECT COUNT(*) FROM chat WHERE id = ?").getFirstResult();
        return countRows != 0;
    }
    public ChatEntity findByID(int id){
        return entityManager.createQuery(
                "SELECT * FROM ChatEntity ch WHERE ch.id = ?", ChatEntity.class
        ).getSingleResult();
    }
}
