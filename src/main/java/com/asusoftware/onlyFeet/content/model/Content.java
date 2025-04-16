package com.asusoftware.onlyFeet.content.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "content")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID creatorId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String mediaType; // 'image' | 'video'

    @Column(nullable = false)
    private List<String> mediaUrls;

    private String visibility; // 'public' | 'subscribers_only'

    private String category;

    @Column
    private String[] tags;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean isDeleted;

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
