package com.asusoftware.onlyFeet.request.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "custom_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomRequest {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID creatorId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String requestText;

    @Column(nullable = false)
    private BigDecimal offerAmount;

    @Column(nullable = false)
    private String status; // 'pending', 'accepted', 'rejected', 'delivered'

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
