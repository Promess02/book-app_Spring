package com.miko.bookapp.repo.repoSQL;

import com.miko.bookapp.model.User;
import com.miko.bookapp.repo.UserRepo;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepoSQL extends JpaRepository<User, Long>, UserRepo {

    @Override
    boolean existsByEmail(String email);

    @Override
    Optional<User> findByEmail(String email);

    @Override
    @Query(nativeQuery = true, value = "select if(max(id) is null, 0, max(id)+1) from user")
    long getNextGeneratedId();
}
