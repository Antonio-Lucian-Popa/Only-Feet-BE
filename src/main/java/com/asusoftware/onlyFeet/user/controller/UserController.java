package com.asusoftware.onlyFeet.user.controller;

import com.asusoftware.onlyFeet.user.model.dto.UserProfileResponseDto;
import com.asusoftware.onlyFeet.user.model.dto.UserRegisterRequestDto;
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

    @PostMapping("/register")
    public ResponseEntity<UserProfileResponseDto> register(@RequestBody UserRegisterRequestDto request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponseDto> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @GetMapping("/{creatorId}/profile")
    public ResponseEntity<UserProfileResponseDto> getCreatorProfile(
            @PathVariable UUID creatorId,
            @RequestParam(required = false) UUID viewerId
    ) {
        return ResponseEntity.ok(userService.getProfile(creatorId, viewerId));
    }

}
