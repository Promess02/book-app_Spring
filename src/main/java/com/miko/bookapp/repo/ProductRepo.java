package com.miko.bookapp.repo;

import com.miko.bookapp.model.Product;

public interface ProductRepo extends RepoTemplate<Product>{
    Product saveEntity(Product entity);

}
