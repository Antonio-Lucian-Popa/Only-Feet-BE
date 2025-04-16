package com.asusoftware.onlyFeet.content.model.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class LikeRequestDto {
    private UUID userId;
    private UUID contentId;
}
