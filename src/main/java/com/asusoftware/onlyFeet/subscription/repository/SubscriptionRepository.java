package com.asusoftware.onlyFeet.subscription.repository;

import com.asusoftware.onlyFeet.subscription.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findBySubscriberIdAndCreatorIdAndIsActiveTrue(UUID subscriberId, UUID creatorId);
    List<Subscription> findBySubscriberId(UUID subscriberId);
    List<Subscription> findByCreatorId(UUID creatorId);
}