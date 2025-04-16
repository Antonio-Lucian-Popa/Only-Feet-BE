package com.asusoftware.onlyFeet.stripe.service;

import com.asusoftware.onlyFeet.payment.model.Payment;
import com.asusoftware.onlyFeet.payment.model.PaymentStatus;
import com.asusoftware.onlyFeet.payment.model.PaymentType;
import com.asusoftware.onlyFeet.payment.repository.PaymentRepository;
import com.asusoftware.onlyFeet.stripe.model.dto.StripeCheckoutRequestDto;
import com.asusoftware.onlyFeet.subscription.model.Subscription;
import com.asusoftware.onlyFeet.subscription.repository.SubscriptionRepository;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StripeService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Value("${stripe.success-url}")
    private String defaultSuccessUrl;

    @Value("${stripe.cancel-url}")
    private String defaultCancelUrl;

    public String createCheckoutSession(StripeCheckoutRequestDto req) {
        try {
            SessionCreateParams.Builder builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(req.getSuccessUrl() != null ? req.getSuccessUrl() : defaultSuccessUrl)
                    .setCancelUrl(req.getCancelUrl() != null ? req.getCancelUrl() : defaultCancelUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount(req.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(req.getType().name())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .putMetadata("userId", req.getUserId().toString())
                    .putMetadata("type", req.getType().name());

            if (req.getContentId() != null) {
                builder.putMetadata("contentId", req.getContentId().toString());
            }

            Session session = Session.create(builder.build());

            return session.getUrl();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create Stripe Checkout Session", e);
        }
    }

    public void saveSuccessfulPayment(UUID userId, UUID contentId, Long amountInCents, String typeRaw) {
        BigDecimal amount = BigDecimal.valueOf(amountInCents).divide(BigDecimal.valueOf(100));

        // 1. Salvează plata
        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .contentId(contentId)
                .amount(amount)
                .type(typeRaw)
                .status(PaymentStatus.SUCCEEDED.name())
                .paymentMethod("stripe_checkout")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        // 2. Activează abonament dacă e de tip SUBSCRIPTION
        if (typeRaw.equalsIgnoreCase(PaymentType.SUBSCRIPTION.name()) && contentId != null) {
            UUID creatorId = contentId; // folosim contentId ca referință către creator (stripe metadata)
            LocalDateTime now = LocalDateTime.now();

            Subscription subscription = Subscription.builder()
                    .id(UUID.randomUUID())
                    .subscriberId(userId)
                    .creatorId(creatorId)
                    .startDate(now)
                    .endDate(now.plusMonths(1))
                    .isActive(true)
                    .priceAtPurchase(amount)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            subscriptionRepository.save(subscription);
        }
    }
}
