package com.asusoftware.onlyFeet.user.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Data
@Builder
public class UserProfileResponseDto {
    private UUID id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private String bio;

    // ✅ Evităm prefixul `is` pentru consistență în JSON
    private boolean creator;
    private boolean subscribed;
}