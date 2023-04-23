package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.entities.Chat;
import javax.sql.DataSource;

public class JDBCChatDAO extends JDBCDAO {
    @Autowired
    public JDBCChatDAO(DataSource dataSource) {
        super(dataSource);
    }
    public void add(Chat newChat){
        jdbcTemplate.update(
                "insert into chat (id) values (?)",
                newChat.getId());
    }
    public void remove(int idChat){
        jdbcTemplate.update(
                "delete from chat where id = ?",
                idChat);
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
