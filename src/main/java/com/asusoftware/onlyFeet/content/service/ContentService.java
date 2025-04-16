package com.asusoftware.onlyFeet.content.service;

import com.asusoftware.onlyFeet.comment.model.dto.CommentDto;
import com.asusoftware.onlyFeet.comment.repository.CommentRepository;
import com.asusoftware.onlyFeet.content.model.Content;
import com.asusoftware.onlyFeet.content.model.dto.ContentCreateRequestDto;
import com.asusoftware.onlyFeet.content.model.dto.ContentDetailsDto;
import com.asusoftware.onlyFeet.content.model.dto.ContentResponseDto;
import com.asusoftware.onlyFeet.content.repository.ContentRepository;
import com.asusoftware.onlyFeet.rating.model.Rating;
import com.asusoftware.onlyFeet.rating.repository.RatingRepository;
import com.asusoftware.onlyFeet.reaction.model.ReactionType;
import com.asusoftware.onlyFeet.reaction.repository.ReactionRepository;

import com.asusoftware.onlyFeet.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final FileStorageService fileStorageService;
    private final RatingRepository ratingRepository;
    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;

    public ContentResponseDto upload(List<MultipartFile> files, ContentCreateRequestDto request) {
        List<String> uploadedUrls = fileStorageService.storeFiles(files, request.getCreatorId());

        Content content = Content.builder()
                .creatorId(request.getCreatorId())
                .title(request.getTitle())
                .description(request.getDescription())
                .mediaType(request.getMediaType())
                .mediaUrls(uploadedUrls)
                .visibility(request.getVisibility())
                .category(request.getCategory())
                .tags(request.getTags())
                .isDeleted(false)
                .build();

        return mapToResponse(content, true); // full access după upload
    }

    public List<ContentResponseDto> getPublicContent() {
        return contentRepository.findByVisibility("public").stream()
                .map(content -> mapToResponse(content, true))
                .toList();
    }

    public List<ContentResponseDto> getContentForViewer(UUID creatorId, UUID viewerId) {
        List<Content> contentList = contentRepository.findByCreatorId(creatorId);

        boolean isSubscribed = viewerId != null && !viewerId.equals(creatorId)
                && subscriptionRepository
                .findBySubscriberIdAndCreatorIdAndIsActiveTrue(viewerId, creatorId)
                .isPresent();

        return contentList.stream()
                .map(content -> mapToResponse(content, isSubscribed || content.getVisibility().equals("public")))
                .toList();
    }

    /**
     * Retrieves content details for a specific content ID.
     * @param contentId The ID of the content.
     * @param viewerId The ID of the viewer (optional).
     * @return ContentDetailsDto containing detailed information about the content.
     */
    public ContentDetailsDto getContentDetails(UUID contentId, UUID viewerId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new NoSuchElementException("Content not found"));

        boolean isAccessible = content.getVisibility().equals("public");

        if (!isAccessible && viewerId != null && !viewerId.equals(content.getCreatorId())) {
            isAccessible = subscriptionRepository
                    .findBySubscriberIdAndCreatorIdAndIsActiveTrue(viewerId, content.getCreatorId())
                    .isPresent();
        }

        // Ratings
        List<Rating> ratings = ratingRepository.findByContentId(contentId);
        double avgRating = ratings.stream().mapToInt(Rating::getScore).average().orElse(0);
        int totalRatings = ratings.size();

        // Reacții ❤️ (LOVE)
        int likeCount = reactionRepository.countByContentIdAndType(contentId, ReactionType.LOVE);

        // Comments
        List<CommentDto> comments = commentRepository.findByContentId(contentId).stream()
                .map(comment -> {
                    CommentDto dto = new CommentDto();
                    dto.setId(comment.getId());
                    dto.setUserId(comment.getUserId());
                    dto.setText(comment.getText());
                    dto.setCreatedAt(comment.getCreatedAt());
                    return dto;
                })
                .toList();

        ContentDetailsDto dto = new ContentDetailsDto();
        dto.setId(content.getId());
        dto.setCreatorId(content.getCreatorId());
        dto.setTitle(isAccessible ? content.getTitle() : "🔒 Conținut protejat");
        dto.setDescription(isAccessible ? content.getDescription() : null);
        dto.setMediaType(content.getMediaType());
        dto.setMediaUrls(isAccessible ? content.getMediaUrls() : List.of("/assets/blurred-thumbnail.jpg"));
        dto.setVisibility(content.getVisibility());
        dto.setCategory(content.getCategory());
        dto.setTags(content.getTags());
        dto.setCreatedAt(content.getCreatedAt());
        dto.setAccessible(isAccessible);
        dto.setAverageRating(avgRating);
        dto.setTotalRatings(totalRatings);
        dto.setLikeCount(likeCount);
        dto.setComments(isAccessible ? comments : List.of());

        return dto;
    }

    public List<String> getAllCategories() {
        return contentRepository.findAllDistinctCategories();
    }

    public List<ContentResponseDto> getByCreator(UUID creatorId) {
        return contentRepository.findByCreatorId(creatorId).stream()
                .map(content -> mapToResponse(content, true)) // creatorul are acces la tot
                .toList();
    }

    public Optional<Content> getRawById(UUID id) {
        return contentRepository.findById(id);
    }

    /**
     * Transforms a Content entity into a DTO.
     * @param content The content entity.
     * @param accessible If true, full data is exposed. If false, returns blurred content.
     */
    private ContentResponseDto mapToResponse(Content content, boolean accessible) {
        ContentResponseDto dto = new ContentResponseDto();
        dto.setId(content.getId());
        dto.setCreatorId(content.getCreatorId());
        dto.setTitle(accessible ? content.getTitle() : "🔒 Abonament necesar");
        dto.setDescription(accessible ? content.getDescription() : null);
        dto.setMediaType(content.getMediaType());
        dto.setMediaUrl(accessible
                ? (content.getMediaUrls() != null && !content.getMediaUrls().isEmpty() ? content.getMediaUrls().get(0) : null)
                : "/assets/blurred-thumbnail.jpg");
        dto.setVisibility(content.getVisibility());
        dto.setCategory(content.getCategory());
        dto.setTags(content.getTags());
        dto.setCreatedAt(content.getCreatedAt());
        dto.setAccessible(accessible);
        return dto;
    }
}
