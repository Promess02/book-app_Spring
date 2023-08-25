package com.miko.bookapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
}
