package com.asusoftware.onlyFeet.subscription.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SubscriptionRequestDto {
    private UUID subscriberId;
    private UUID creatorId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal priceAtPurchase;
}
