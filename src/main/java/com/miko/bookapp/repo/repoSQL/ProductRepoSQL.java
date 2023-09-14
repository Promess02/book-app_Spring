package com.miko.bookapp.repo.repoSQL;

import com.miko.bookapp.model.Product;
import com.miko.bookapp.repo.ProductRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepoSQL extends JpaRepository<Product, Long>, ProductRepo {
}
