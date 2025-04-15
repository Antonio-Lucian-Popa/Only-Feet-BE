package com.asusoftware.onlyFeet.subscription.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID subscriberId;

    @Column(nullable = false)
    private UUID creatorId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean isActive;

    private BigDecimal priceAtPurchase;

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
