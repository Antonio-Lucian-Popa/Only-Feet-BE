package com.asusoftware.onlyFeet.stripe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StripeAccountRepository extends JpaRepository<StripeAccountRepository, UUID> {
    Optional<StripeAccountRepository> findByUserId(UUID userId);
}
