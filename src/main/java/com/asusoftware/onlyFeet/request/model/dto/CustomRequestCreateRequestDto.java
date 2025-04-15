package com.asusoftware.onlyFeet.request.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CustomRequestCreateRequestDto {
    private UUID userId;
    private UUID creatorId;
    private String requestText;
    private BigDecimal offerAmount;
}
