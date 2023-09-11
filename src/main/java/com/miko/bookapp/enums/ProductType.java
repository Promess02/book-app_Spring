package com.miko.bookapp.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductType {
    BOOK("book"), BUNDLE("bundle");

    private final String name;
    ProductType(String name){
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static ProductType fromValue(String value) {
        if(value== null) return null;
        for (ProductType productType : ProductType.values()) {
            if (productType.name.equalsIgnoreCase(value)) {
                return productType;
            }
        }
        throw new IllegalArgumentException("Invalid Product type: " + value);
    }

    @Override
    public String toString() {
        return name;
    }
}
