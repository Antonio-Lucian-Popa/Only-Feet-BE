package com.asusoftware.onlyFeet.request.service;

import com.asusoftware.onlyFeet.request.model.CustomRequest;
import com.asusoftware.onlyFeet.request.model.dto.CustomRequestCreateRequestDto;
import com.asusoftware.onlyFeet.request.model.dto.CustomRequestResponseDto;
import com.asusoftware.onlyFeet.request.repository.CustomRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomRequestService {

    private final CustomRequestRepository customRequestRepository;

    public CustomRequestResponseDto create(CustomRequestCreateRequestDto req) {
        if (req.getUserId().equals(req.getCreatorId())) {
            throw new IllegalArgumentException("You can't request custom content from yourself.");
        }

        boolean hasPending = customRequestRepository.findByUserId(req.getUserId())
                .stream()
                .anyMatch(r -> r.getCreatorId().equals(req.getCreatorId()) && r.getStatus().equals("pending"));

        if (hasPending) {
            throw new IllegalStateException("You already have a pending request for this creator.");
        }

        CustomRequest entity = CustomRequest.builder()
                .id(UUID.randomUUID())
                .userId(req.getUserId())
                .creatorId(req.getCreatorId())
                .requestText(req.getRequestText())
                .offerAmount(req.getOfferAmount())
                .status("pending")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return mapToResponse(customRequestRepository.save(entity));
    }

    public List<CustomRequestResponseDto> getRequestsForCreator(UUID creatorId) {
        return customRequestRepository.findByCreatorId(creatorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CustomRequestResponseDto> getRequestsByUser(UUID userId) {
        return customRequestRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CustomRequestResponseDto updateStatus(UUID requestId, String newStatus) {
        CustomRequest req = customRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        req.setStatus(newStatus);
        req.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(customRequestRepository.save(req));
    }

    private CustomRequestResponseDto mapToResponse(CustomRequest r) {
        CustomRequestResponseDto dto = new CustomRequestResponseDto();
        dto.setId(r.getId());
        dto.setUserId(r.getUserId());
        dto.setCreatorId(r.getCreatorId());
        dto.setRequestText(r.getRequestText());
        dto.setOfferAmount(r.getOfferAmount());
        dto.setStatus(r.getStatus());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
