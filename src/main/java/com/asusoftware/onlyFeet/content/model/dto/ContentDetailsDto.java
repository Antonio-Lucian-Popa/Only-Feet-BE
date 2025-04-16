package com.asusoftware.onlyFeet.content.model.dto;

import com.asusoftware.onlyFeet.comment.model.dto.CommentDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ContentDetailsDto {
    private UUID id;
    private UUID creatorId;

    private String title;
    private String description;

    private String mediaType;
    private List<String> mediaUrls; // toate fișierele

    private String visibility;
    private String category;
    private String[] tags;

    private LocalDateTime createdAt;

    private boolean isAccessible;

    // 🔢 Informații extra (social)
    private double averageRating;
    private int totalRatings;
    private int likeCount;

    // 💬 Lista de comentarii (simplă)
    private List<CommentDto> comments;
}
