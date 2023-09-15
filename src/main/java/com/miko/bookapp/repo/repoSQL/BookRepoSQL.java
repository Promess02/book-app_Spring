package com.miko.bookapp.repo.repoSQL;

import com.miko.bookapp.model.Book;
import com.miko.bookapp.repo.BookRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepoSQL extends BookRepo, JpaRepository<Book, Long> {
    @Override
    @Query(nativeQuery = true, value = "select if(max(id) is null, 0, max(id)+1) from book")
    long getNextGeneratedId();


    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO Book (id, description, price, isbn, name, author, book_category, book_type, bundle_id) " +
            "VALUES (:id, :description, :price, :isbn, :name, :author, :bookCategory, :bookType, :bundle_id)")
    void saveBook(@Param("id") Long id,
                  @Param("description") String description,
                  @Param("price") Double price,
                  @Param("isbn") int isbn,
                  @Param("name") String name,
                  @Param("author") String author,
                  @Param("bookCategory") String bookCategory,
                  @Param("bookType") String bookType,
                  @Param("bundle_id") Long bookBundle);

    @Override
    default Book saveEntity(Book book) {
        var description = book.getDescription()==null?null:book.getDescription();
        var bookCategory = book.getBookCategory()==null?null:book.getBookCategory().toString();
        var bookType = book.getBookType()==null?null:book.getBookType().toString();
        Long bookBundle = book.getBookBundle()==null?null:book.getBookBundle().getId();
        saveBook(
                book.getId(),
                description,
                book.getPrice(),
                book.getIsbn(),
                book.getName(),
                book.getAuthor(),
                bookCategory,
                bookType,
                bookBundle
        );

        // Return the saved Book object
        return book;
    }
}
