package com.miko.bookapp.service;

import com.miko.bookapp.model.Order;
import com.miko.bookapp.model.User;
import com.miko.bookapp.repo.UserRepo;

import java.util.*;

public class MemoryUserRepo implements UserRepo {
    private int index = 0;
    private Map<Long, User> map = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public User save(User entity) {
        if (entity.getId()==0){
            try{
                var field = User.class.getDeclaredField("id");
                field.setAccessible(true);
                field.set(entity,++index);
            }catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        map.put(entity.getId(), entity);

        return entity;      }

    @Override
    public Optional<User> findById(long id) {
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
    public void delete(User entity) {
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

    @Override
    public boolean existsByEmail(String email) {
        for(User user: map.values())
            if(user.getEmail().equals(email)) return true;
        return false;
    }

    @Override
    public Optional<User> findByEmail(String email) {

        for(User user: map.values())
            if(user.getEmail().equals(email)) return Optional.of(user);

        return Optional.empty();
    }
}
