package com.miko.bookapp.repo;

import com.miko.bookapp.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepo extends RepoTemplate<Book>{
    Book saveEntity(Book entity);
}
