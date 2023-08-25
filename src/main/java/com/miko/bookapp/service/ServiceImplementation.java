package com.miko.bookapp.service;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.repo.BookRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceImplementation implements ServiceBook{

    private final BookRepo bookRepo;
    @Override
    public List<Book> getAllBooks() {
        log.info("Retrieving all the books");
        return bookRepo.findAll();
    }

    @Override
    public Book saveBook(Book book) {
        log.info("Saving book");
        return bookRepo.save(book);
    }

    @Override
    public Optional<Book> findBookById(long id) {
        if(bookRepo.existsById(id)) return bookRepo.findById(id);
        else return Optional.empty();

    }

    @Override
    public Optional<Book> changeBook(long id, Book book) {
        if (bookRepo.existsById(id)){
            Optional<Book> result = Optional.of(book);
            result.get().setId(id);
             bookRepo.save(result.get());
             return result;
        }else return Optional.empty();
    }

    @Override
    public Optional<Book> updateBook(long id, Book book) {
        if (bookRepo.existsById(id)){
            Class<?> bookClass = book.getClass();
            Field[] bookFields = bookClass.getDeclaredFields();

            Optional<Book> bookToUpdate = bookRepo.findById(id);
            for(Field field: bookFields){
                field.setAccessible(true);
                try {
                    Object sourceValue = field.get(book);
                    if(sourceValue != null && !field.getName().equals("id")){
                        field.set(bookToUpdate.get(), sourceValue);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            bookToUpdate.get().setId(id);
            bookRepo.save(bookToUpdate.get());
            return bookToUpdate;
        }else return Optional.empty();
    }


    @Override
    public Optional<Book> deleteBookById(long id) {
        if (bookRepo.existsById(id)){
            bookRepo.deleteById(id);
            return bookRepo.findById(id);
        }else return Optional.empty();
    }
}
