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
//    @Column(name = "book_category")
//    @NotBlank(message = "category cannot be blank")
//    private BookCategory bookCategory;
//    @Column(name = "book_type")
//    @NotBlank(message = "book type cannot be blank")
//    private BookType bookType;
//    @NotBlank(message = "prize cannot be blank")
//    private Double prize;
    private String description;
}
