package com.miko.bookapp.service;

import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.model.Product;
import com.miko.bookapp.repo.ProductRepo;

import java.util.*;

public class MemoryProductRepo implements ProductRepo {
    private int index = 0;
    private Map<Long, Product> map = new HashMap<>();


    @Override
    public Product saveEntity(Product entity) {
        if (entity.getId()==0){
            try{
                var field = Product.class.getDeclaredField("id");
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
    public List<Product> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Product save(Product entity) {
        if (entity.getId()==0){
            try{
                var field = Product.class.getDeclaredField("id");
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
    public Optional<Product> findById(long id) {
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
    public void delete(Product entity) {
        map.remove(entity.getId(),entity);

    }

    @Override
    public void deleteAll() {
        map.clear();
    }

    @Override
    public long getNextGeneratedId() {
        return map.size()+1;
    }
}
