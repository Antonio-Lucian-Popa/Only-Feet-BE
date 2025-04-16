package com.asusoftware.onlyFeet.user.controller;

import com.asusoftware.onlyFeet.user.model.dto.UserProfileResponseDto;
import com.asusoftware.onlyFeet.user.model.dto.UserRegisterDto;
import com.asusoftware.onlyFeet.user.model.dto.UserResponseDto;
import com.asusoftware.onlyFeet.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Înregistrare user nou (create user în Keycloak + local).
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterDto request) {
        return ResponseEntity.ok(userService.register(request));
    }

    /**
     * Obține profilul unui user (cu viewer optional pentru status abonament).
     */
    @GetMapping("/{creatorId}/profile")
    public ResponseEntity<UserProfileResponseDto> getUserProfile(
            @PathVariable UUID creatorId,
            @RequestParam(required = false) UUID viewerId
    ) {
        return ResponseEntity.ok(userService.getProfile(creatorId, viewerId));
    }
}
