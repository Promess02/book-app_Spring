package com.miko.bookapp.repo;

import com.miko.bookapp.model.Book;

import java.util.List;
import java.util.Optional;

public interface RepoTemplate<T, ID> {
    List<T> findAll();

    T save(T entity);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAll();
}
