package com.asusoftware.onlyFeet.user.model.dto;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String email;
    private String password;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String userRole; // "USER", "CREATOR", "ADMIN"
}
