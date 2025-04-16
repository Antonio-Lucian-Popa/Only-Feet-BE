package com.asusoftware.onlyFeet.content.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ContentResponseDto {
    private UUID id;
    private UUID creatorId;
    private String title;
    private String description;
    private String mediaType;
    private String mediaUrl; // poate fi blurred dacă e blocat
    private String visibility;
    private String category;
    private String[] tags;
    private LocalDateTime createdAt;
    private boolean isAccessible; // << important
}
