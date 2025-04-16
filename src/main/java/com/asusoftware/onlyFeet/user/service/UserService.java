package com.asusoftware.onlyFeet.user.service;

import com.asusoftware.onlyFeet.config.KeycloakService;
import com.asusoftware.onlyFeet.subscription.repository.SubscriptionRepository;
import com.asusoftware.onlyFeet.user.model.User;
import com.asusoftware.onlyFeet.user.model.UserRole;
import com.asusoftware.onlyFeet.user.model.dto.*;
import com.asusoftware.onlyFeet.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final KeycloakService keycloakService;
    private final ModelMapper mapper;

    /**
     * Înregistrare în Keycloak + salvare locală
     */
    @Transactional
    public UserResponseDto register(UserRegisterDto dto) {
        String keycloakId = keycloakService.createKeycloakUser(dto);

        User user = User.builder()
                .id(UUID.randomUUID())
                .keycloakId(keycloakId)
                .email(dto.getEmail())
                .username(dto.getUsername())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phoneNumber(dto.getPhoneNumber())
                .userRole(UserRole.valueOf(dto.getUserRole()))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        userRepository.save(user);
        return mapper.map(user, UserResponseDto.class);
    }

    /**
     * Autentificare cu Keycloak (obține token JWT)
     */
    public AccessTokenResponse login(LoginDto loginDto) {
        return keycloakService.loginUser(loginDto);
    }

    /**
     * Obține profilul fără viewer (fără info despre abonament)
     */
    public UserProfileResponseDto getProfile(UUID userId) {
        return getProfile(userId, null);
    }

    /**
     * Obține profilul complet, cu verificare abonament viewer → creator
     */
    public UserProfileResponseDto getProfile(UUID creatorId, UUID viewerId) {
        User user = userRepository.findById(creatorId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        boolean subscribed = isViewerSubscribed(viewerId, creatorId);

        return mapToUserProfileDto(user, subscribed);
    }

    private UserProfileResponseDto mapToUserProfileDto(User user, boolean subscribed) {
        return UserProfileResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePictureUrl(user.getProfilePictureUrl())
                .bio(user.getBio())
                .creator(user.getUserRole() == UserRole.CREATOR)
                .subscribed(subscribed)
                .build();
    }


    /**
     * Verifică dacă viewer-ul este abonat la creator
     */
    private boolean isViewerSubscribed(UUID viewerId, UUID creatorId) {
        if (viewerId == null || viewerId.equals(creatorId)) {
            return false;
        }
        return subscriptionRepository
                .findBySubscriberIdAndCreatorIdAndIsActiveTrue(viewerId, creatorId)
                .isPresent();
    }
}
