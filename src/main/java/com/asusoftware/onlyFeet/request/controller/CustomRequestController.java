package com.asusoftware.onlyFeet.request.controller;

import com.asusoftware.onlyFeet.request.model.dto.CustomRequestCreateRequestDto;
import com.asusoftware.onlyFeet.request.model.dto.CustomRequestResponseDto;
import com.asusoftware.onlyFeet.request.service.CustomRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/custom-requests")
@RequiredArgsConstructor
public class CustomRequestController {

    private final CustomRequestService customRequestService;

    @PostMapping
    public ResponseEntity<CustomRequestResponseDto> create(@RequestBody CustomRequestCreateRequestDto req) {
        return ResponseEntity.ok(customRequestService.create(req));
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<CustomRequestResponseDto>> getForCreator(@PathVariable UUID creatorId) {
        return ResponseEntity.ok(customRequestService.getRequestsForCreator(creatorId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CustomRequestResponseDto>> getForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(customRequestService.getRequestsByUser(userId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CustomRequestResponseDto> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(customRequestService.updateStatus(id, status));
    }
}
