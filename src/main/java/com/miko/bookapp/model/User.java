package com.miko.bookapp.model;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Entity
@Data
@Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String email;
    private String password;
    private boolean premiumStatus;
}
