
package com.kartikay.streamhub.service;

import com.kartikay.streamhub.dto.ContentRequest;
import com.kartikay.streamhub.entity.Content;
import com.kartikay.streamhub.exception.ResourceNotFoundException;
import com.kartikay.streamhub.repository.ContentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    // Create new content
    public Content create(ContentRequest request) {
        Content content = Content.builder()
                .title(request.getTitle())
                .genre(request.getGenre())
                .description(request.getDescription())
                .releaseYear(request.getReleaseYear())
                .rating(request.getRating())
                .build();

        return contentRepository.save(content);
    }

    // Get all content
    public List<Content> getAll() {
        return contentRepository.findAll();
    }

    // Get paginated content
    public Page<Content> getAll(Pageable pageable) {
        return contentRepository.findAll(pageable);
    }

    // Cache content by ID
    @Cacheable(value = "contentById", key = "#p0")
    public Content getById(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Content not found"
                        )
                );
    }

    // Search by title
    public List<Content> searchByTitle(String keyword) {
        return contentRepository
                .findByTitleContainingIgnoreCase(keyword);
    }

    // Filter by genre
    public List<Content> getByGenre(String genre) {
        return contentRepository.findByGenreIgnoreCase(genre);
    }

    // Update content and refresh Redis cache
    @CachePut(value = "contentById", key = "#p0")
    public Content update(Long id, ContentRequest request) {

        Content content = getById(id);

        content.setTitle(request.getTitle());
        content.setGenre(request.getGenre());
        content.setDescription(request.getDescription());
        content.setReleaseYear(request.getReleaseYear());
        content.setRating(request.getRating());

        return contentRepository.save(content);
    }

    // Delete content and remove Redis cache entry
    @CacheEvict(value = "contentById", key = "#p0")
    public void delete(Long id) {

        Content content = getById(id);
        contentRepository.delete(content);
    }
}
