package com.asusoftware.onlyFeet.content.controller;

import com.asusoftware.onlyFeet.content.model.dto.ContentFeedDto;
import com.asusoftware.onlyFeet.content.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<List<ContentFeedDto>> getFeed(
            @RequestParam UUID viewerId,
            @RequestParam(defaultValue = "recent") String sort,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(feedService.getFeed(viewerId, sort, category, page, size));
    }

}
