package com.asusoftware.onlyFeet.content.service;

import com.asusoftware.onlyFeet.content.model.Content;
import com.asusoftware.onlyFeet.content.model.dto.ContentCreateRequestDto;
import com.asusoftware.onlyFeet.content.model.dto.ContentResponseDto;
import com.asusoftware.onlyFeet.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;

    public ContentResponseDto upload(ContentCreateRequestDto request) {
        Content content = Content.builder()
                .creatorId(request.getCreatorId())
                .title(request.getTitle())
                .description(request.getDescription())
                .mediaType(request.getMediaType())
                .mediaUrl(request.getMediaUrl())
                .visibility(request.getVisibility())
                .category(request.getCategory())
                .tags(request.getTags())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        return mapToResponse(contentRepository.save(content));
    }

    public List<ContentResponseDto> getPublicContent() {
        return contentRepository.findByVisibility("public")
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ContentResponseDto> getByCreator(UUID creatorId) {
        return contentRepository.findByCreatorId(creatorId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Optional<Content> getRawById(UUID id) {
        return contentRepository.findById(id);
    }

    private ContentResponseDto mapToResponse(Content content) {
        ContentResponseDto dto = new ContentResponseDto();
        dto.setId(content.getId());
        dto.setCreatorId(content.getCreatorId());
        dto.setTitle(content.getTitle());
        dto.setDescription(content.getDescription());
        dto.setMediaType(content.getMediaType());
        dto.setMediaUrl(content.getMediaUrl());
        dto.setVisibility(content.getVisibility());
        dto.setCategory(content.getCategory());
        dto.setTags(content.getTags());
        dto.setCreatedAt(content.getCreatedAt());
        return dto;
    }
}
