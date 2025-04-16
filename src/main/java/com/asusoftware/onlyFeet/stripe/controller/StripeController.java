package com.asusoftware.onlyFeet.stripe.controller;

import com.asusoftware.onlyFeet.stripe.model.dto.StripeCheckoutRequestDto;
import com.asusoftware.onlyFeet.stripe.service.StripeService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @PostMapping("/checkout")
    public ResponseEntity<String> createCheckoutSession(@RequestBody StripeCheckoutRequestDto request) {
        String url = stripeService.createCheckoutSession(request);
        return ResponseEntity.ok(url);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(HttpServletRequest request,
                                                @RequestHeader("Stripe-Signature") String sigHeader) {
        String payload;
        try (BufferedReader reader = request.getReader()) {
            payload = reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid payload");
        }

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("⚠️ Webhook signature verification failed.");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

            if (session != null && session.getMetadata() != null) {
                UUID userId = UUID.fromString(session.getMetadata().get("userId"));
                String type = session.getMetadata().get("type");
                UUID contentId = session.getMetadata().containsKey("contentId")
                        ? UUID.fromString(session.getMetadata().get("contentId"))
                        : null;
                Long totalAmount = session.getAmountTotal();

                stripeService.saveSuccessfulPayment(userId, contentId, totalAmount, type);
            }
        }

        return ResponseEntity.ok("✅ Webhook received");
    }
}
