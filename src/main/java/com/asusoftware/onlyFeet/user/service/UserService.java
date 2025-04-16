package com.asusoftware.onlyFeet.user.service;

import com.asusoftware.onlyFeet.subscription.repository.SubscriptionRepository;
import com.asusoftware.onlyFeet.user.model.User;
import com.asusoftware.onlyFeet.user.model.dto.UserProfileResponseDto;
import com.asusoftware.onlyFeet.user.model.dto.UserRegisterRequestDto;
import com.asusoftware.onlyFeet.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository; // folosit pentru a verifica daca utilizatorul este abonat
    private final PasswordEncoder passwordEncoder; // folosit pentru hash

    public UserProfileResponseDto register(UserRegisterRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already used.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .isCreator(request.isCreator())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    public UserProfileResponseDto getProfile(UUID userId) {
        return getProfile(userId, null); // doar o redirectare
    }

    public UserProfileResponseDto getProfile(UUID creatorId, UUID viewerId) {
        User user = userRepository.findById(creatorId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        boolean subscribed = false;

        if (viewerId != null && !viewerId.equals(creatorId)) {
            subscribed = subscriptionRepository
                    .findBySubscriberIdAndCreatorIdAndIsActiveTrue(viewerId, creatorId)
                    .isPresent();
        }

        UserProfileResponseDto dto = new UserProfileResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        dto.setBio(user.getBio());
        dto.setCreator(user.isCreator());
        dto.setSubscribed(subscribed);

        return dto;
    }

    private UserProfileResponseDto mapToResponse(User user) {
        UserProfileResponseDto dto = new UserProfileResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        dto.setBio(user.getBio());
        dto.setCreator(user.isCreator());
        return dto;
    }
}