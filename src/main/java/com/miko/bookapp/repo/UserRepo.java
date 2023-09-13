package com.miko.bookapp.repo;

import com.miko.bookapp.model.User;

import java.util.Optional;

public interface UserRepo extends RepoTemplate<User>{
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
