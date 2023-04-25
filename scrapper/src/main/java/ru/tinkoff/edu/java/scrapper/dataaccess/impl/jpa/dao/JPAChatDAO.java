package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import ru.tinkoff.edu.java.scrapper.dataaccess.ChatDAService;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities.ChatEntity;
import ru.tinkoff.edu.java.scrapper.entities.Chat;

import javax.sql.DataSource;

public class JPAChatDAO extends JPADAO {
    public void add(Chat newChat){
        entityManager.persist(newChat);
    }
    public void remove(int idChat){
        entityManager.createQuery("delete from chat where chat.id = idChat");
    }
    public boolean containsChatWithId(int chatId){
        int countRows = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM chat WHERE id = ?", int.class, chatId);
        return countRows != 0;
    }
    public Chat findByID(int id){
        return jdbcTemplate.queryForObject(
                "SELECT * FROM chat WHERE id = ?",
                new Object[] {id},
                new BeanPropertyRowMapper<>(Chat.class)
        );
    }
}
