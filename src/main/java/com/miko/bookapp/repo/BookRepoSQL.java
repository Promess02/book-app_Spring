package com.miko.bookapp.repo;

import com.miko.bookapp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepoSQL extends BookRepo, JpaRepository<Book, Long> {

}
