package com.asusoftware.onlyFeet.poll.service;

import com.asusoftware.onlyFeet.poll.model.Poll;
import com.asusoftware.onlyFeet.poll.model.PollVote;
import com.asusoftware.onlyFeet.poll.model.dto.PollCreateRequestDto;
import com.asusoftware.onlyFeet.poll.model.dto.PollResponseDto;
import com.asusoftware.onlyFeet.poll.model.dto.PollVoteRequestDto;
import com.asusoftware.onlyFeet.poll.model.dto.PollVoteResponseDto;
import com.asusoftware.onlyFeet.poll.repository.PollRepository;
import com.asusoftware.onlyFeet.poll.repository.PollVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;
    private final PollVoteRepository pollVoteRepository;

    public PollResponseDto create(PollCreateRequestDto request) {
        Poll poll = Poll.builder()
                .id(UUID.randomUUID())
                .creatorId(request.getCreatorId())
                .question(request.getQuestion())
                .options(request.getOptions().toArray(new String[0]))
                .createdAt(LocalDateTime.now())
                .expiresAt(request.getExpiresAt())
                .build();

        return mapPollToResponse(pollRepository.save(poll));
    }

    public PollVoteResponseDto vote(PollVoteRequestDto request) {
        Poll poll = pollRepository.findById(request.getPollId())
                .orElseThrow(() -> new NoSuchElementException("Poll not found"));

        if (poll.getExpiresAt() != null && poll.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Poll has expired.");
        }

        boolean alreadyVoted = pollVoteRepository.findByPollIdAndUserId(
                request.getPollId(), request.getUserId()).isPresent();

        if (alreadyVoted) {
            throw new IllegalStateException("You already voted in this poll.");
        }

        if (!Arrays.asList(poll.getOptions()).contains(request.getSelectedOption())) {
            throw new IllegalArgumentException("Invalid poll option.");
        }

        PollVote vote = PollVote.builder()
                .id(UUID.randomUUID())
                .pollId(request.getPollId())
                .userId(request.getUserId())
                .selectedOption(request.getSelectedOption())
                .votedAt(LocalDateTime.now())
                .build();

        return mapVoteToResponse(pollVoteRepository.save(vote));
    }

    public List<PollResponseDto> getPollsByCreator(UUID creatorId) {
        return pollRepository.findByCreatorId(creatorId)
                .stream()
                .map(this::mapPollToResponse)
                .collect(Collectors.toList());
    }

    private PollResponseDto mapPollToResponse(Poll poll) {
        PollResponseDto dto = new PollResponseDto();
        dto.setId(poll.getId());
        dto.setCreatorId(poll.getCreatorId());
        dto.setQuestion(poll.getQuestion());
        dto.setOptions(List.of(poll.getOptions()));
        dto.setCreatedAt(poll.getCreatedAt());
        dto.setExpiresAt(poll.getExpiresAt());
        return dto;
    }

    private PollVoteResponseDto mapVoteToResponse(PollVote vote) {
        PollVoteResponseDto dto = new PollVoteResponseDto();
        dto.setId(vote.getId());
        dto.setPollId(vote.getPollId());
        dto.setUserId(vote.getUserId());
        dto.setSelectedOption(vote.getSelectedOption());
        dto.setVotedAt(vote.getVotedAt());
        return dto;
    }
}
