package com.miko.bookapp.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    PAYMENT_FAILED("payment_failed"), ORDERED("ordered"), SHIPPED("shipped"), DELIVERED("delivered"), REFUNDED("refunded");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static OrderStatus fromValue(String value) {
        if(value == null) return null;
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.name.equalsIgnoreCase(value)) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus: " + value);
    }

    @Override
    public String toString() {
        return name;
    }
}
