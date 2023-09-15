package com.miko.bookapp.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderReadDTO {
    @NotBlank
    private String userEmail;

    @NotBlank
    private List<OrderItemDTO> orderItems;

    private Double totalAmount;

    public OrderReadDTO(){
        orderItems = new ArrayList<>();
    }

    public OrderReadDTO(String userEmail, List<OrderItemDTO> orderItems) {
        this.userEmail = userEmail;
        this.orderItems = orderItems;
    }


}
