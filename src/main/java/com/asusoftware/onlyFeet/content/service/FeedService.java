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
        List<Content> allContent = contentRepository.findAll();

        // ✅ Filtru după categorie (dacă e specificat)
        if (category != null && !category.isBlank()) {
            allContent = allContent.stream()
                    .filter(c -> category.equalsIgnoreCase(c.getCategory()))
                    .toList();
        }

        Map<UUID, Double> ratingAvgMap = new HashMap<>();
        Map<UUID, Integer> ratingCountMap = new HashMap<>();

        for (Content c : allContent) {
            List<Rating> ratings = ratingRepository.findByContentId(c.getId());
            double avg = ratings.stream().mapToInt(Rating::getScore).average().orElse(0);
            ratingAvgMap.put(c.getId(), avg);
            ratingCountMap.put(c.getId(), ratings.size());
        }

        Set<UUID> creatorIdsSubscribed = subscriptionRepository.findBySubscriberId(viewerId).stream()
                .filter(Subscription::isActive)
                .map(Subscription::getCreatorId)
                .collect(Collectors.toSet());

        List<ContentFeedDto> result = allContent.stream()
                .filter(content -> {
                    if ("public".equals(content.getVisibility())) return true;
                    return creatorIdsSubscribed.contains(content.getCreatorId());
                })
                .map(content -> {
                    boolean accessible = content.getVisibility().equals("public") ||
                            creatorIdsSubscribed.contains(content.getCreatorId());

                    ContentFeedDto dto = new ContentFeedDto();
                    dto.setId(content.getId());
                    dto.setCreatorId(content.getCreatorId());
                    dto.setTitle(accessible ? content.getTitle() : "🔒 Abonament necesar");
                    dto.setDescription(accessible ? content.getDescription() : null);
                    dto.setMediaUrl(accessible ? content.getMediaUrl() : "/assets/blurred-thumbnail.jpg");
                    dto.setAccessible(accessible);
                    dto.setCreatedAt(content.getCreatedAt());
                    dto.setRatingAvg(ratingAvgMap.getOrDefault(content.getId(), 0.0));
                    dto.setRatingCount(ratingCountMap.getOrDefault(content.getId(), 0));
                    return dto;
                })
                .sorted((a, b) -> {
                    if ("popular".equalsIgnoreCase(sort)) {
                        return Double.compare(b.getRatingAvg(), a.getRatingAvg());
                    } else {
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                })
                .toList();

        // ✅ Paginare simplă (offset)
        int fromIndex = Math.min(page * size, result.size());
        int toIndex = Math.min(fromIndex + size, result.size());

        return result.subList(fromIndex, toIndex);
    }

}

