package com.asusoftware.onlyFeet.payment.model.dto;


import com.asusoftware.onlyFeet.payment.model.PaymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentCreateRequestDto {
    private UUID userId;
    private UUID contentId; // optional
    private BigDecimal amount;
    private PaymentType type;
    private String paymentMethod; // Stripe: card / wallet / apple_pay etc.
}
