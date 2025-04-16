package com.asusoftware.onlyFeet.content.controller;

import com.asusoftware.onlyFeet.content.model.dto.ContentCreateRequestDto;
import com.asusoftware.onlyFeet.content.model.dto.ContentDetailsDto;
import com.asusoftware.onlyFeet.content.model.dto.ContentResponseDto;
import com.asusoftware.onlyFeet.content.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    /*
     * Endpoint pentru upload de conținut (imagini sau video)
     * @param files Lista de fișiere (imagini sau video)
     * @param data Datele conținutului (titlu, descriere, etc.)
     * @return Detaliile conținutului încărcat
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ContentResponseDto> uploadContent(
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("data") ContentCreateRequestDto data
    ) {
        return ResponseEntity.ok(contentService.upload(files, data));
    }

    /*
     * Endpoint pentru a obține conținutul public
     * @return Lista de conținut public
     */
    @GetMapping("/public")
    public ResponseEntity<List<ContentResponseDto>> getPublicContent() {
        return ResponseEntity.ok(contentService.getPublicContent());
    }

    /*
     * Endpoint pentru a obține conținutul pentru un creator specificat
     * @param creatorId ID-ul creatorului
     * @param viewerId ID-ul vizitatorului (opțional)
     * @return Lista de conținut pentru creatorul specificat
     */
    @GetMapping("/creator/{creatorId}/viewer")
    public ResponseEntity<List<ContentResponseDto>> getContentForViewer(
            @PathVariable UUID creatorId,
            @RequestParam(required = false) UUID viewerId
    ) {
        return ResponseEntity.ok(contentService.getContentForViewer(creatorId, viewerId));
    }

    /**
     * Endpoint pentru a obține toate categoriile disponibile
     * @return Lista de categorii
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(contentService.getAllCategories());
    }

    /**
     * Endpoint pentru a obține detaliile unui conținut specificat
     * @param id ID-ul conținutului
     * @param viewerId ID-ul vizitatorului (opțional)
     * @return Detaliile conținutului specificat
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContentDetailsDto> getContentDetails(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID viewerId
    ) {
        return ResponseEntity.ok(contentService.getContentDetails(id, viewerId));
    }

    /**
     * Endpoint pentru a obține conținutul creat de un creator specificat
     * @param creatorId ID-ul creatorului
     * @return Lista de conținut creat de creatorul specificat
     */
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<ContentResponseDto>> getByCreator(@PathVariable UUID creatorId) {
        return ResponseEntity.ok(contentService.getByCreator(creatorId));
    }
}
