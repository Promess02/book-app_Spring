package com.miko.bookapp.enums;

public enum BookType {
    HARD_COVER("hard_cover"), SOFT_COVER("soft_cover"), AUDIOBOOK("audiobook"), EBOOK("ebook");

    private String name;

    BookType(String text) {
        this.name = text;
    }

    public String getName() {
        return name;
    }
}
