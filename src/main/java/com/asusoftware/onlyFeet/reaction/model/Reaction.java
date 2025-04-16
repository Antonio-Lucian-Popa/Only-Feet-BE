package com.asusoftware.onlyFeet.reaction.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reactions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "content_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaction {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "content_id", nullable = false)
    private UUID contentId;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

