package com.miko.bookapp.enums;

public enum BookType {
    HARDCOVER("hardcover"), PAPERBACK("paperback"), EBOOK("ebook"), AUDIOBOOK("audiobook");

    private String name;

    BookType(String name) {
        this.name = name;
    }
}
