package com.asusoftware.onlyFeet.poll.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "polls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Poll {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID creatorId;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String[] options;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
