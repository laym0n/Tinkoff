package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Setter;

@Setter
public abstract class JPADAO {
    @PersistenceContext
    protected EntityManager entityManager;
}
