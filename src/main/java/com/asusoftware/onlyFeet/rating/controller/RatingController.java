package com.asusoftware.onlyFeet.rating.controller;

import com.asusoftware.onlyFeet.rating.model.dto.RatingRequestDto;
import com.asusoftware.onlyFeet.rating.model.dto.RatingResponseDto;
import com.asusoftware.onlyFeet.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<Void> rate(@RequestBody RatingRequestDto dto) {
        ratingService.rate(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/content/{contentId}")
    public ResponseEntity<RatingResponseDto> getRating(@PathVariable UUID contentId) {
        return ResponseEntity.ok(ratingService.getStats(contentId));
    }
}

