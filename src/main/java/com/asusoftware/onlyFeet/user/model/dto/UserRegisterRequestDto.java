package com.asusoftware.onlyFeet.user.model.dto;

import lombok.Data;

@Data
public class UserRegisterRequestDto {
    private String username;
    private String email;
    private String password;
    private boolean isCreator;
}
