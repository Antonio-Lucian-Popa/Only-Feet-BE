package com.asusoftware.onlyFeet.request.repository;

import com.asusoftware.onlyFeet.request.model.CustomRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomRequestRepository extends JpaRepository<CustomRequest, UUID> {
    List<CustomRequest> findByUserId(UUID userId);
    List<CustomRequest> findByCreatorId(UUID creatorId);
}
