package com.asusoftware.onlyFeet.payment.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    private UUID contentId; // opțional: dacă plata e pentru un conținut specific

    @Column(nullable = false)
    private BigDecimal amount;

    private String paymentMethod;

    private String type; // 'subscription', 'custom_content', 'tip'

    private String status; // 'pending', 'succeeded', 'failed'

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
