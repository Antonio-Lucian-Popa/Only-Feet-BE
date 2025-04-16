package com.asusoftware.onlyFeet.rating.service;

import com.asusoftware.onlyFeet.rating.model.Rating;
import com.asusoftware.onlyFeet.rating.model.dto.RatingRequestDto;
import com.asusoftware.onlyFeet.rating.model.dto.RatingResponseDto;
import com.asusoftware.onlyFeet.rating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public void rate(RatingRequestDto dto) {
        if (dto.getScore() < 1 || dto.getScore() > 5)
            throw new IllegalArgumentException("Rating must be between 1 and 5");

        Optional<Rating> existing = ratingRepository.findByUserIdAndContentId(dto.getUserId(), dto.getContentId());

        if (existing.isPresent()) {
            Rating rating = existing.get();
            rating.setScore(dto.getScore());
            rating.setCreatedAt(LocalDateTime.now());
            ratingRepository.save(rating);
        } else {
            Rating rating = Rating.builder()
                    .userId(dto.getUserId())
                    .contentId(dto.getContentId())
                    .score(dto.getScore())
                    .createdAt(LocalDateTime.now())
                    .build();
            ratingRepository.save(rating);
        }
    }

    public RatingResponseDto getStats(UUID contentId) {
        List<Rating> ratings = ratingRepository.findByContentId(contentId);
        double avg = ratings.stream().mapToInt(Rating::getScore).average().orElse(0);

        RatingResponseDto dto = new RatingResponseDto();
        dto.setContentId(contentId);
        dto.setAverageRating(avg);
        dto.setTotalRatings(ratings.size());
        return dto;
    }

}

