package com.miko.bookapp.repo.repoSQL;

import com.miko.bookapp.enums.BookCategory;
import com.miko.bookapp.enums.BookType;
import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.repo.BundleRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BundleRepoSQL extends BundleRepo, JpaRepository<BookBundle, Long> {
    @Override
    @Query(nativeQuery = true, value = "select if(max(id) is null, 0, max(id)+1) from bundle")
    long getNextGeneratedId();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO Bundle (id, description, price, discount) " +
            "VALUES (:id, :description, :price, :discount)")
    void saveBundle(@Param("id") Long id,
                  @Param("description") String description,
                  @Param("price") Double price,
                  @Param("discount") Double discount);


    @Override
    default BookBundle saveEntity(BookBundle bundle) {
        saveBundle(
                bundle.getId(),
                bundle.getDescription(),
                bundle.getPrice(),
                bundle.getDiscount()
        );

        return bundle;
    }
}
