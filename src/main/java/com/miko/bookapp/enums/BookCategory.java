package com.miko.bookapp.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BookCategory {
    FANTASY("fantasy"), ROMANCE("romance"), SCIENCE_FICTION("science_fiction"), BIOGRAPHY("biography"), HORROR("horror"), CRIME("crime"), DRAMA("drama");

    private final String name;
    BookCategory(String name){
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static BookCategory fromValue(String value) {
        for (BookCategory category : BookCategory.values()) {
            if (category.name.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid BookCategory: " + value);
    }

    @Override
    public String toString() {
        return name;
    }
}
