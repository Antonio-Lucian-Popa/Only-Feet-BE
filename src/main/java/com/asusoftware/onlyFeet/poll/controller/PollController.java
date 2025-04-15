package com.asusoftware.onlyFeet.poll.controller;

import com.asusoftware.onlyFeet.poll.model.dto.PollCreateRequestDto;
import com.asusoftware.onlyFeet.poll.model.dto.PollResponseDto;
import com.asusoftware.onlyFeet.poll.model.dto.PollVoteRequestDto;
import com.asusoftware.onlyFeet.poll.model.dto.PollVoteResponseDto;
import com.asusoftware.onlyFeet.poll.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/polls")
@RequiredArgsConstructor
public class PollController {

    private final PollService pollService;

    @PostMapping
    public ResponseEntity<PollResponseDto> createPoll(@RequestBody PollCreateRequestDto request) {
        return ResponseEntity.ok(pollService.create(request));
    }

    @PostMapping("/vote")
    public ResponseEntity<PollVoteResponseDto> vote(@RequestBody PollVoteRequestDto request) {
        return ResponseEntity.ok(pollService.vote(request));
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<PollResponseDto>> getPollsByCreator(@PathVariable UUID creatorId) {
        return ResponseEntity.ok(pollService.getPollsByCreator(creatorId));
    }
}