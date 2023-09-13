package com.miko.bookapp.repo.repoSQL;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.repo.BookRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepoSQL extends BookRepo, JpaRepository<Book, Long> {

}
