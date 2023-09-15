package com.miko.bookapp.repo.repoSQL;

import com.miko.bookapp.enums.BookCategory;
import com.miko.bookapp.enums.BookType;
import com.miko.bookapp.model.Book;
import com.miko.bookapp.model.BookBundle;
import com.miko.bookapp.model.Product;
import com.miko.bookapp.repo.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepoSQL extends JpaRepository<Product, Long>, ProductRepo {
    @Override
    @Query(nativeQuery = true, value = "select if(max(id) is null, 0, max(id)+1) from product")
    long getNextGeneratedId();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO product (id, description, price) " +
            "VALUES (:id, :description, :price)")
    void saveProduct(@Param("id") Long id,
                  @Param("description") String description,
                  @Param("price") Double price);

    @Override
    default Product saveEntity(Product entity) {
        saveProduct(
                entity.getId(),
                entity.getDescription(),
                entity.getPrice()
        );

        return entity;
    }
}
