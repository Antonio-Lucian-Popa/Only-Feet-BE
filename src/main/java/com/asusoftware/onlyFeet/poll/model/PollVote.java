package com.asusoftware.onlyFeet.poll.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "poll_votes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollVote {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID pollId;

    @Column(nullable = false)
    private UUID userId;

    private String selectedOption;

    private LocalDateTime votedAt;

    @PrePersist
    public void onCreate() {
        votedAt = LocalDateTime.now();
    }
}
