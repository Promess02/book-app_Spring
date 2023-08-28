package com.miko.bookapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miko.bookapp.enums.BookCategory;
import com.miko.bookapp.enums.BookType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(columnDefinition = "VARCHAR(40)", name = "book_category")
    @Enumerated(EnumType.STRING)
    private BookCategory bookCategory;

    @Column(columnDefinition = "VARCHAR(40)", name = "book_type")
    @Enumerated(EnumType.STRING)
    private BookType bookType;

    @Column
    @NotBlank(message = "prize cannot be blank")
    private Double prize;

    @ManyToOne
    @JoinColumn(name = "bundle_id")
    private BookBundle bookBundle;
}
