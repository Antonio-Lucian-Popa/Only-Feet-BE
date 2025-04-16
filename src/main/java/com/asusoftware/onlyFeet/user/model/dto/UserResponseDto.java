package com.asusoftware.onlyFeet.user.model.dto;

import com.asusoftware.onlyFeet.user.model.UserRole;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID id;
    private String email;
    private String username;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    private UserRole userRole;

    private String profilePictureUrl;
    private String bio;

    private Instant createdAt;
    private Instant updatedAt;
}
