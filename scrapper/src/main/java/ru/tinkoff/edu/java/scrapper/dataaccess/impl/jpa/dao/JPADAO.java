package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

public abstract class JPADAO {
    protected EntityManager entityManager;
    protected JdbcTemplate jdbcTemplate;
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }
}