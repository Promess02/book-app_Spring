package com.miko.bookapp.model;

import com.miko.bookapp.enums.BookCategory;
import com.miko.bookapp.enums.BookType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@Table(name = "book")
@Entity
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "isbn cannot be blank")
    private int isbn;
    @NotBlank(message = "name cannot be blank")
    private String name;
    @NotBlank(message = "author cannot be blank")
    private String author;
    private String description;

    @Column(name = "book_category")
    @Enumerated(EnumType.STRING)
    private BookCategory bookCategory;



    @Column( name = "book_type")
    @Enumerated(EnumType.STRING)
    private BookType bookType;

    @Column
    @NotBlank(message = "prize cannot be blank")
    private Double prize;

    @ManyToOne
    @JoinColumn(name = "bundle_id")
    private BookBundle bookBundle;

    Book(){}
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Double getPrize() {
        return prize;
    }

    void setPrize(Double prize) {
        this.prize = prize;
    }

    public BookBundle getBookBundle() {
        return bookBundle;
    }

    public void setBookBundle(BookBundle bookBundle) {
        this.bookBundle = bookBundle;
    }
}
