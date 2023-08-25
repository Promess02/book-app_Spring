package com.miko.bookapp.enums;

public enum BookCategory {
    FANTASY("fantasy"), SCIENCE_FICTION("science-fiction"), ROMANCE("romance"), HORROR("horror"), CRIME("crime"), BIOGRAPHY("biography");
    
    private String name;

    BookCategory(String text) {
        this.name = text;
    }

    public String getName() {
        return name;
    }
}
