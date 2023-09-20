package com.miko.bookapp;

import com.miko.bookapp.DTO.OrderItemDTO;
import com.miko.bookapp.DTO.OrderReadDTO;
import com.miko.bookapp.model.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

public class Dummy {

    public static Book dummyBook(int id, String desc){
        return new Book(id, 47,"book", "author", desc, null, null, 56.3,null);
    }

    public static BookBundle dummyBundle(int id, String desc){
        return new BookBundle(id,desc, 0.45,0d, new HashSet<>());
    }

    public static User dummyUser(long id, String email){
        return new User(id, email,"secret",false,null,0d);
    }

    public static Order dummyOrder(long id, Double totalAmount){

        return new Order(id, dummyUser(0,"miko2002"), Collections.emptyList(), LocalDateTime.now(), totalAmount);
    }

    public static OrderReadDTO dummyOrderDTO(String email, Double totalAmount){
        return new OrderReadDTO(email,null,totalAmount);
    }


    public static Product dummyProductDTO(long id, String desc){
        return new Product(id, desc,0d);
    }


}
