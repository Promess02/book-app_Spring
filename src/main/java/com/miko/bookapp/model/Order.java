package com.miko.bookapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "custom_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "custom_order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private LocalDateTime orderDate;

    private Double totalAmount;

    Order(){
        orderItems = new ArrayList<>();
    }

    public Order(Long id, User user, List<OrderItem> orderItems, LocalDateTime orderDate, Double totalAmount) {
        this.id = id;
        this.user = user;
        this.orderItems = orderItems;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        calculateOrderTotalAmount();
    }
    public void removeOrderItem(OrderItem orderItem){
        orderItems.remove(orderItem);
        calculateOrderTotalAmount();
    }

    private void calculateOrderTotalAmount(){
        double temp = 0d;
        for(OrderItem orderItem: orderItems){
            temp = temp + orderItem.getTotalPrice();
        }
        totalAmount = temp;
    }

}