package com.asusoftware.onlyFeet.poll.repository;


import com.asusoftware.onlyFeet.poll.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PollRepository extends JpaRepository<Poll, UUID> {
    List<Poll> findByCreatorId(UUID creatorId);
}