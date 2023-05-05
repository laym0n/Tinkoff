package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jooq.dao;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class JOOQDAO {
    @Autowired
    protected DSLContext context;
}
