package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

public abstract class JDBCDAO {
    protected JdbcTemplate jdbcTemplate;
    public JDBCDAO(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        TransactionTemplate asd = new TransactionTemplate();
    }
}
