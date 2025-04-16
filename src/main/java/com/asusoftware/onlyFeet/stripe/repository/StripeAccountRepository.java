package com.asusoftware.onlyFeet.stripe.repository;

import com.asusoftware.onlyFeet.stripe.model.StripeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StripeAccountRepository extends JpaRepository<StripeAccount, UUID> {
    Optional<StripeAccountRepository> findByUserId(UUID userId);
}
