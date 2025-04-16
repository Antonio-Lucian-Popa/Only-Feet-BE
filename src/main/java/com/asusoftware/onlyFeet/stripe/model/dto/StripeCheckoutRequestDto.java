package com.asusoftware.onlyFeet.stripe.model.dto;

import com.asusoftware.onlyFeet.payment.model.PaymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class StripeCheckoutRequestDto {
    private UUID userId;
    private UUID contentId; // opțional
    private BigDecimal amount;
    private PaymentType type;
    private String successUrl; // override dacă vrei
    private String cancelUrl;
}
