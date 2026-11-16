
package com.kartikay.streamhub.controller;

import com.kartikay.streamhub.dto.ContentRequest;
import com.kartikay.streamhub.entity.Content;
import com.kartikay.streamhub.service.ContentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Set;


import java.util.List;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    // NEW: Pagination and sorting    
    @GetMapping
    public Page<Content> getAll(
            @ParameterObject
            @PageableDefault(size = 10, sort = "title")
            Pageable pageable) {

        if (pageable.getPageSize() > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page size cannot exceed 100"
            );
        }

        if (pageable.getPageSize() < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page size must be at least 1"
            );
        }

        Set<String> allowedSortFields = Set.of(
                "id", "title", "genre", "releaseYear", "rating"
        );

        for (Sort.Order order : pageable.getSort()) {
            if (!allowedSortFields.contains(order.getProperty())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid sort field: " + order.getProperty()
                );
            }
        }

        return contentService.getAll(pageable);
    }


    // Get content by ID
    @GetMapping("/{id}")
    public Content getById(@PathVariable("id") Long id) {
        return contentService.getById(id);
    }

    // Search content by title
    @GetMapping("/search")
    public List<Content> search(
            @RequestParam("keyword") String keyword) {

        return contentService.searchByTitle(keyword);
    }

    // Filter content by genre
    @GetMapping("/genre/{genre}")
    public List<Content> getByGenre(
            @PathVariable("genre") String genre) {

        return contentService.getByGenre(genre);
    }

    // Admin: Create content
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Content create(
            @Valid @RequestBody ContentRequest request) {

        return contentService.create(request);
    }

    // Admin: Update content
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Content update(
            @PathVariable("id") Long id,
            @Valid @RequestBody ContentRequest request) {

        return contentService.update(id, request);
    }

    // Admin: Delete content
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        contentService.delete(id);
    }
}
