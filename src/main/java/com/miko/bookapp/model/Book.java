package com.miko.bookapp.model;

import com.miko.bookapp.enums.BookCategory;
import com.miko.bookapp.enums.BookType;
import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;

@Table(name = "book")
@Entity
public class Book extends Product{
    @NotBlank(message = "isbn cannot be blank")
    private int isbn;
    @NotBlank(message = "name cannot be blank")
    private String name;
    @NotBlank(message = "author cannot be blank")
    private String author;
    @Column(name = "book_category")
    @Enumerated(EnumType.STRING)
    private BookCategory bookCategory;

    @Column( name = "book_type")
    @Enumerated(EnumType.STRING)
    private BookType bookType;

    @ManyToOne
    @JoinColumn(name = "bundle_id")
    private BookBundle bookBundle;

    Book(){}

    public Book(long id, int isbn,String name, String author, String description, BookCategory bookCategory, BookType bookType,Double price,  BookBundle bookBundle) {
        super(id, description, price);
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.bookCategory = bookCategory;
        this.bookType = bookType;
        this.bookBundle = bookBundle;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    public BookType getBookType() {
        return bookType;
    }

     void setBookType(BookType bookType) {
        this.bookType = bookType;
    }

    public BookBundle getBookBundle() {
        return bookBundle;
    }

    public void setBookBundle(BookBundle bookBundle) {
        this.bookBundle = bookBundle;
    }
}
