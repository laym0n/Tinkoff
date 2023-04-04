package ru.tinkoff.edu.java.scrapper.dataaccess;

import java.util.Optional;

public interface CRUDService<T, K> {
    T create(T entity);
    Optional<T> findById(K idEntity);
    void update(T entity);
    void delete(K idEntity);
}
