package com.asusoftware.onlyFeet.subscription.controller;

import com.asusoftware.onlyFeet.subscription.model.dto.SubscriptionRequestDto;
import com.asusoftware.onlyFeet.subscription.model.dto.SubscriptionResponseDto;
import com.asusoftware.onlyFeet.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponseDto> subscribe(@RequestBody SubscriptionRequestDto request) {
        return ResponseEntity.ok(subscriptionService.subscribe(request));
    }

    @GetMapping("/user/{subscriberId}")
    public ResponseEntity<List<SubscriptionResponseDto>> getByUser(@PathVariable UUID subscriberId) {
        return ResponseEntity.ok(subscriptionService.findBySubscriber(subscriberId));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isSubscribed(
            @RequestParam UUID subscriberId,
            @RequestParam UUID creatorId
    ) {
        return ResponseEntity.ok(subscriptionService.isSubscribed(subscriberId, creatorId));
    }
}
