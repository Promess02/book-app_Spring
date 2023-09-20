package com.miko.bookapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Order custom_order;

    @ManyToOne
    private Product product;  // Product can be either Book or BookBundle

    private int quantity;

    private Double totalPrice;

    public OrderItem(){}

    public OrderItem(Order custom_order, Product product, int quantity) {
        this.custom_order = custom_order;
        this.product = product;
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public OrderItem(Long id, Order custom_order, Product product, int quantity) {
        this.id = id;
        this.custom_order = custom_order;
        this.product = product;
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return custom_order;
    }

    public void setOrder(Order order) {
        this.custom_order = order;
    }

    public Product getProduct() {

        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        calculateTotalPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public double getTotalPrice() {
        calculateTotalPrice();
        return totalPrice;
    }


    private void calculateTotalPrice(){
        totalPrice = quantity * product.getPrice();
    }

}