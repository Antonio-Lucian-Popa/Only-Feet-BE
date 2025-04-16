package com.asusoftware.onlyFeet.content.controller;

import com.asusoftware.onlyFeet.content.model.dto.ContentCreateRequestDto;
import com.asusoftware.onlyFeet.content.model.dto.ContentResponseDto;
import com.asusoftware.onlyFeet.content.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @PostMapping
    public ResponseEntity<ContentResponseDto> uploadContent(@RequestBody ContentCreateRequestDto request) {
        return ResponseEntity.ok(contentService.upload(request));
    }

    @GetMapping("/public")
    public ResponseEntity<List<ContentResponseDto>> getPublicContent() {
        return ResponseEntity.ok(contentService.getPublicContent());
    }

    @GetMapping("/creator/{creatorId}/viewer")
    public ResponseEntity<List<ContentResponseDto>> getContentForViewer(
            @PathVariable UUID creatorId,
            @RequestParam(required = false) UUID viewerId
    ) {
        return ResponseEntity.ok(contentService.getContentForViewer(creatorId, viewerId));
    }


    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<ContentResponseDto>> getByCreator(@PathVariable UUID creatorId) {
        return ResponseEntity.ok(contentService.getByCreator(creatorId));
    }
}
