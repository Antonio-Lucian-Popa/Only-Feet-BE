package com.asusoftware.onlyFeet.content.model.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ContentCreateRequestDto {
    private UUID creatorId;
    private String title;
    private String description;
    private String mediaType;
    private List<String> mediaUrls; // în loc de mediaUrl simplu
    private String visibility;
    private String category;
    private String[] tags;
}
