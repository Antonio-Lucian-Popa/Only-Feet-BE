package com.asusoftware.onlyFeet.request.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CustomRequestResponseDto {
    private UUID id;
    private UUID userId;
    private UUID creatorId;
    private String requestText;
    private BigDecimal offerAmount;
    private String status;
    private LocalDateTime createdAt;
}
