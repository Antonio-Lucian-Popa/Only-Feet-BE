package com.asusoftware.onlyFeet.poll.repository;

import com.asusoftware.onlyFeet.poll.model.PollVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PollVoteRepository extends JpaRepository<PollVote, UUID> {
    Optional<PollVote> findByPollIdAndUserId(UUID pollId, UUID userId);
}
