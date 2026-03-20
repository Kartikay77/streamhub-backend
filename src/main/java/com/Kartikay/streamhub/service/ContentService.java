package com.kartikay.streamhub.service;

import com.kartikay.streamhub.dto.ContentRequest;
import com.kartikay.streamhub.entity.Content;
import com.kartikay.streamhub.exception.ResourceNotFoundException;
import com.kartikay.streamhub.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

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

    public List<Content> getAll() {
        return contentRepository.findAll();
    }

    public Content getById(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found"));
    }

    public List<Content> searchByTitle(String keyword) {
        return contentRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Content> getByGenre(String genre) {
        return contentRepository.findByGenreIgnoreCase(genre);
    }

    public Content update(Long id, ContentRequest request) {
        Content content = getById(id);
        content.setTitle(request.getTitle());
        content.setGenre(request.getGenre());
        content.setDescription(request.getDescription());
        content.setReleaseYear(request.getReleaseYear());
        content.setRating(request.getRating());
        return contentRepository.save(content);
    }

    public void delete(Long id) {
        Content content = getById(id);
        contentRepository.delete(content);
    }
}