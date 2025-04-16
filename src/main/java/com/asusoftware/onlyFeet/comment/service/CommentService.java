package com.asusoftware.onlyFeet.comment.service;

import com.asusoftware.onlyFeet.comment.model.Comment;
import com.asusoftware.onlyFeet.comment.model.dto.CommentRequestDto;
import com.asusoftware.onlyFeet.comment.model.dto.CommentResponseDto;
import com.asusoftware.onlyFeet.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentResponseDto add(CommentRequestDto dto) {
        Comment comment = Comment.builder()
                .userId(dto.getUserId())
                .contentId(dto.getContentId())
                .text(dto.getText())
                .createdAt(LocalDateTime.now())
                .build();

        Comment saved = commentRepository.save(comment);

        return mapToResponse(saved);
    }

    public List<CommentResponseDto> getByContent(UUID contentId) {
        return commentRepository.findByContentId(contentId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CommentResponseDto mapToResponse(Comment e) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(e.getId());
        dto.setUserId(e.getUserId());
        dto.setContentId(e.getContentId());
        dto.setText(e.getText());
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }
}

