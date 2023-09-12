package com.miko.bookapp.repo;

import com.miko.bookapp.model.User;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepoSQL extends JpaRepository<User, Long>, UserRepo {

    @Override
    boolean existsByEmail(String email);

    @Override
    Optional<User> findByEmail(String email);
}
