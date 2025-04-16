package com.asusoftware.onlyFeet.user.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserProfileResponseDto {
    private UUID id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private String bio;
    private boolean isCreator;
    private boolean isSubscribed;
}