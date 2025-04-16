package com.asusoftware.onlyFeet.comment.controller;

import com.asusoftware.onlyFeet.comment.model.dto.CommentRequestDto;
import com.asusoftware.onlyFeet.comment.model.dto.CommentResponseDto;
import com.asusoftware.onlyFeet.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> add(@RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.add(dto));
    }

    @GetMapping("/content/{contentId}")
    public ResponseEntity<List<CommentResponseDto>> getAll(@PathVariable UUID contentId) {
        return ResponseEntity.ok(commentService.getByContent(contentId));
    }
}
