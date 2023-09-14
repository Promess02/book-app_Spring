package com.miko.bookapp.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

public class OrderItemDTO {

    private long custom_order_id;

    @NotBlank
    private long product_id;  // Product can be either Book or BookBundle

    @NotBlank
    private int quantity;



    public OrderItemDTO(long custom_order_id, long product_id, int quantity) {
        this.custom_order_id = custom_order_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public long getCustom_order_id() {
        return custom_order_id;
    }

    public void setCustom_order_id(long custom_order_id) {
        this.custom_order_id = custom_order_id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
