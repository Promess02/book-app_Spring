package com.miko.bookapp.service;

import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.repo.BundleRepo;

import java.util.*;

public class MemoryBundleRepo implements BundleRepo {
    private int index = 0;
    private Map<Long, BookBundle> map = new HashMap<>();
    @Override
    public List<BookBundle> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public BookBundle save(BookBundle entity) {
        if (entity.getBundleID()==0){
            try{
                var field = BookBundle.class.getDeclaredField("bundleID");
                field.setAccessible(true);
                field.set(entity,++index);
            }catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        map.put(entity.getBundleID(), entity);

        return entity;
    }

    @Override
    public Optional<BookBundle> findById(long id) {
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
    public void delete(BookBundle entity) {
        map.remove(entity.getBundleID(),entity);
    }

    @Override
    public void deleteAll() {
        map.clear();
    }
}
