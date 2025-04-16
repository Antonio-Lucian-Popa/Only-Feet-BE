package com.asusoftware.onlyFeet.payment.model.dto;

import com.asusoftware.onlyFeet.payment.model.PaymentStatus;
import com.asusoftware.onlyFeet.payment.model.PaymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentResponseDto {
    private UUID id;
    private UUID userId;
    private UUID contentId;
    private BigDecimal amount;
    private String paymentMethod;
    private PaymentType type;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}
