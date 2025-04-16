package com.asusoftware.onlyFeet.content.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ContentFeedDto {
    private UUID id;
    private UUID creatorId;
    private String title;
    private String description;
    private String mediaUrl;
    private boolean isAccessible;
    private double ratingAvg;
    private int ratingCount;
    private LocalDateTime createdAt;
}

