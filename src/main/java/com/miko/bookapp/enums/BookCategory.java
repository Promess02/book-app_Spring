package com.miko.bookapp.enums;

public enum BookCategory {
    FANTASY("fantasy"), ROMANCE("romance"), SCIENCE_FICTION("science_fiction"), BIOGRAPHY("biography"), HORROR("horror"), CRIME("crime"), DRAMA("drama");

    private final String name;
    BookCategory(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
