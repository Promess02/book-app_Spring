package com.miko.bookapp.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String description;

    private Double price;

    Product(){
        price = 0d;
    }

    public Product(long id, String description, Double price) {
        this.id = id;
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //when saved to the db it first saves a product to a product table and then a specific product to table that inherits from product
    // so the specificProduct_id = product_id + 1
    public long getSpecificProductId(){
        return id+1;
    }

}
