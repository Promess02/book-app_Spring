package com.miko.bookapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "user")
public class User {

    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String email;

    private String password;
    private boolean premiumStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orderList;

    private Double walletWorth;

    public User() {
        walletWorth = 0d;
    }

    public User(long id, String email, String password, boolean premiumStatus, List<Order> orderList, Double walletWorth) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.premiumStatus = premiumStatus;
        this.orderList = orderList;
        this.walletWorth = walletWorth;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPremiumStatus() {
        return premiumStatus;
    }

    public void setPremiumStatus(boolean premiumStatus) {
        this.premiumStatus = premiumStatus;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public Double getWalletWorth() {
        return walletWorth;
    }

    void setWalletWorth(Double walletWorth) {
        this.walletWorth = walletWorth;
    }

    public void addOrder(Order order){
            decreaseWalletWorth(order.getTotalAmount());
            orderList.add(order);
    }
    public void increaseWalletWorth(double increase){
        walletWorth= walletWorth+increase;
    }

    public void decreaseWalletWorth(double decrease) {
        if (walletWorth > decrease)
            walletWorth = walletWorth - decrease;
        else throw new RuntimeException("not sufficient funds in wallet");
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", premiumStatus=" + premiumStatus +
                ", orderList=" + orderList +
                ", walletWorth=" + walletWorth +
                '}';
    }
}
