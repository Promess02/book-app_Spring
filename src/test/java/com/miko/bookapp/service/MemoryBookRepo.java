package com.miko.bookapp.service;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.repo.BookRepo;

import java.util.*;

public class MemoryBookRepo implements BookRepo {
    private int index = 0;
    private Map<Long, Book> map = new HashMap<>();
    @Override
    public List<Book> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Book save(Book entity) {
        if (entity.getId()==0){
            try{
                var field = Book.class.getSuperclass().getDeclaredField("id");
                field.setAccessible(true);
                field.set(entity,++index);
            }catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        map.put(entity.getId(), entity);

        return entity;
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public boolean existsById(long id) {
        return map.containsKey(id);
    }

    @Override
    public long count() {
        return map.size();
    }

    @Override
    public void deleteById(long id) {
        map.remove(id);
    }

    @Override
    public void delete(Book entity) {
        map.remove(entity);
    }

    @Override
    public void deleteAll() {
        map.clear();
    }

    @Override
    public long getNextGeneratedId() {
        return map.size()+1;
    }

    @Override
    public Book saveEntity(Book entity) {
        if (entity.getId()==0){
            try{
                var field = Book.class.getSuperclass().getDeclaredField("id");
                field.setAccessible(true);
                field.set(entity,++index);
            }catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        map.put(entity.getId(), entity);

        return entity;
    }
}
