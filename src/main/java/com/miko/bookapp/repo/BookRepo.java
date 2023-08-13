package com.miko.bookapp.repo;

import com.miko.bookapp.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepo {

    List<Book> findAll();

    Book save(Book entity);

    Optional<Book> findById(long id);

    boolean existsById(long id);

    long count();

    void deleteById(long id);

    void delete(Book entity);

    void deleteAll();


}
