package com.asusoftware.onlyFeet.subscription.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SubscriptionResponseDto {
    private UUID id;
    private UUID subscriberId;
    private UUID creatorId;
    private boolean isActive;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal priceAtPurchase;
}
