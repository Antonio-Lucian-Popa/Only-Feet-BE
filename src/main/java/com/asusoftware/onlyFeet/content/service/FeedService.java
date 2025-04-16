package com.asusoftware.onlyFeet.content.service;

import com.asusoftware.onlyFeet.content.model.Content;
import com.asusoftware.onlyFeet.content.model.dto.ContentFeedDto;
import com.asusoftware.onlyFeet.content.repository.ContentRepository;
import com.asusoftware.onlyFeet.rating.model.Rating;
import com.asusoftware.onlyFeet.rating.repository.RatingRepository;
import com.asusoftware.onlyFeet.subscription.model.Subscription;
import com.asusoftware.onlyFeet.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final ContentRepository contentRepository;
    private final RatingRepository ratingRepository;
    private final SubscriptionRepository subscriptionRepository;

    public List<ContentFeedDto> getFeed(UUID viewerId, String sort, String category, int page, int size) {
        // 1. Fetch all content
        List<Content> allContent = contentRepository.findAll();

        // 2. Filter by category if needed
        if (category != null && !category.isBlank()) {
            allContent = allContent.stream()
                    .filter(c -> category.equalsIgnoreCase(c.getCategory()))
                    .toList();
        }

        // 3. Preload subscriptions for viewer
        Set<UUID> subscribedCreatorIds = subscriptionRepository.findBySubscriberId(viewerId).stream()
                .filter(Subscription::isActive)
                .map(Subscription::getCreatorId)
                .collect(Collectors.toSet());

        // 4. Preload all ratings per content
        Map<UUID, List<Rating>> ratingsMap = new HashMap<>();
        for (Content content : allContent) {
            ratingsMap.put(content.getId(), ratingRepository.findByContentId(content.getId()));
        }

        // 5. Map to DTOs
        List<ContentFeedDto> feed = allContent.stream()
                .filter(content -> isAccessibleToViewer(content, subscribedCreatorIds))
                .map(content -> {
                    boolean accessible = isAccessibleToViewer(content, subscribedCreatorIds);
                    List<Rating> ratings = ratingsMap.getOrDefault(content.getId(), List.of());

                    double avgRating = ratings.stream().mapToInt(Rating::getScore).average().orElse(0);
                    int totalRatings = ratings.size();

                    ContentFeedDto dto = new ContentFeedDto();
                    dto.setId(content.getId());
                    dto.setCreatorId(content.getCreatorId());
                    dto.setTitle(accessible ? content.getTitle() : "🔒 Abonament necesar");
                    dto.setDescription(accessible ? content.getDescription() : null);
                    dto.setMediaUrl(accessible
                            ? (content.getMediaUrls() != null && !content.getMediaUrls().isEmpty() ? content.getMediaUrls().get(0) : null)
                            : "/assets/blurred-thumbnail.jpg");
                    dto.setAccessible(accessible);
                    dto.setCreatedAt(content.getCreatedAt());
                    dto.setRatingAvg(avgRating);
                    dto.setRatingCount(totalRatings);
                    return dto;
                })
                .sorted((a, b) -> {
                    if ("popular".equalsIgnoreCase(sort)) {
                        return Double.compare(b.getRatingAvg(), a.getRatingAvg());
                    }
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                })
                .toList();

        // 6. Pagination
        int fromIndex = Math.min(page * size, feed.size());
        int toIndex = Math.min(fromIndex + size, feed.size());

        return feed.subList(fromIndex, toIndex);
    }

    private boolean isAccessibleToViewer(Content content, Set<UUID> subscribedCreatorIds) {
        return "public".equals(content.getVisibility()) || subscribedCreatorIds.contains(content.getCreatorId());
    }


}

