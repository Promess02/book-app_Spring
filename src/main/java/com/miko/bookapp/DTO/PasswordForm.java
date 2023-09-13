package com.miko.bookapp.DTO;

import lombok.Data;

@Data
public class PasswordForm {
    private String oldPassword;
    private String newPassword;
    private String email;

}
