package com.kartikay.streamhub.controller;

import com.kartikay.streamhub.dto.ContentRequest;
import com.kartikay.streamhub.entity.Content;
import com.kartikay.streamhub.service.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping
    public List<Content> getAll() {
        return contentService.getAll();
    }

    @GetMapping("/{id}")
    public Content getById(@PathVariable("id") Long id) {
        return contentService.getById(id);
    }

    @GetMapping("/search")
    public List<Content> search(@RequestParam("keyword") String keyword) {
        return contentService.searchByTitle(keyword);
    }

    @GetMapping("/genre/{genre}")
    public List<Content> getByGenre(@PathVariable("genre") String genre) {
        return contentService.getByGenre(genre);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Content create(@Valid @RequestBody ContentRequest request) {
        return contentService.create(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Content update(@PathVariable("id") Long id, @Valid @RequestBody ContentRequest request) {
        return contentService.update(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        contentService.delete(id);
    }
}