package com.miko.bookapp.repo;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.BookBundle;

import java.util.List;
import java.util.Optional;

public interface BundleRepo {
    List<BookBundle> findAll();

    BookBundle save(BookBundle entity);

    Optional<BookBundle> findById(long id);

    boolean existsById(long id);

    long count();

    void deleteById(long id);

    void delete(BookBundle entity);

    void deleteAll();
}
