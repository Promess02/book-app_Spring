package com.miko.bookapp.service;

import com.miko.bookapp.model.Book;

import java.util.List;
import java.util.Optional;

public interface ServiceBook {
    List<Book> getAllBooks();
    Book saveBook(Book book);
    Optional<Book> findBookById(long id);
    Optional<Book> updateBook(long id, Book book);

    Optional<Book> changeBook(long id, Book book);

    Optional<Book> deleteBookById(long id);

    Optional<List<Book>> getFilteredBooks(String category, String type, Double prizeMax);
}
