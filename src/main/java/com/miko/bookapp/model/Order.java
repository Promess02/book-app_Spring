package com.miko.bookapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.miko.bookapp.enums.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "custom_order")
public class Order {
    @Id
    private Long id;

    @ManyToOne
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "custom_order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private LocalDateTime orderDate;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Double totalAmount;

    public Order(){
        orderItems = new ArrayList<>();
        orderDate = LocalDateTime.now();
        orderStatus = OrderStatus.ORDERED;
    }
    public Order(Long id, User user, List<OrderItem> orderItems){
        this();
        this.id = id;
        this.user = user;
        this.orderItems = orderItems;
        calculateOrderTotalAmount();
    }

    public Order(Long id, User user, List<OrderItem> orderItems, LocalDateTime orderDate, Double totalAmount) {
        this(id,user,orderItems);
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
        calculateOrderTotalAmount();
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

}