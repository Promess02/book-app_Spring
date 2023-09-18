package com.miko.bookapp.service;

import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.model.OrderItem;
import com.miko.bookapp.repo.OrderItemRepo;

import java.util.*;

public class MemoryOrderItemRepo implements OrderItemRepo {
    private int index = 0;
    private Map<Long, OrderItem> map = new HashMap<>();


    @Override
    public List<OrderItem> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public OrderItem save(OrderItem entity) {
        if (entity.getId()==0){
            try{
                var field = OrderItem.class.getDeclaredField("id");
                field.setAccessible(true);
                field.set(entity,++index);
            }catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        map.put(entity.getId(), entity);

        return entity;    }

    @Override
    public Optional<OrderItem> findById(long id) {
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
    public void delete(OrderItem entity) {
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
