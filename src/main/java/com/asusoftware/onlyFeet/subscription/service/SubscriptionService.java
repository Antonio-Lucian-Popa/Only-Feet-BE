package com.asusoftware.onlyFeet.subscription.service;

import com.asusoftware.onlyFeet.subscription.model.Subscription;
import com.asusoftware.onlyFeet.subscription.model.dto.SubscriptionRequestDto;
import com.asusoftware.onlyFeet.subscription.model.dto.SubscriptionResponseDto;
import com.asusoftware.onlyFeet.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionResponseDto subscribe(SubscriptionRequestDto request) {
        boolean alreadySubscribed = subscriptionRepository
                .findBySubscriberIdAndCreatorIdAndIsActiveTrue(request.getSubscriberId(), request.getCreatorId())
                .isPresent();

        if (alreadySubscribed) {
            throw new IllegalStateException("You are already subscribed to this creator.");
        }

        Subscription subscription = Subscription.builder()
                .subscriberId(request.getSubscriberId())
                .creatorId(request.getCreatorId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isActive(true)
                .priceAtPurchase(request.getPriceAtPurchase())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return mapToResponse(subscriptionRepository.save(subscription));
    }

    public List<SubscriptionResponseDto> findBySubscriber(UUID subscriberId) {
        return subscriptionRepository.findBySubscriberId(subscriberId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public boolean isSubscribed(UUID subscriberId, UUID creatorId) {
        return subscriptionRepository
                .findBySubscriberIdAndCreatorIdAndIsActiveTrue(subscriberId, creatorId)
                .isPresent();
    }

    private SubscriptionResponseDto mapToResponse(Subscription sub) {
        SubscriptionResponseDto dto = new SubscriptionResponseDto();
        dto.setId(sub.getId());
        dto.setSubscriberId(sub.getSubscriberId());
        dto.setCreatorId(sub.getCreatorId());
        dto.setStartDate(sub.getStartDate());
        dto.setEndDate(sub.getEndDate());
        dto.setPriceAtPurchase(sub.getPriceAtPurchase());
        dto.setActive(sub.isActive());
        return dto;
    }
}
