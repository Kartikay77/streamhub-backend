package com.kartikay.streamhub.service;

import com.kartikay.streamhub.dto.ContentRequest;
import com.kartikay.streamhub.entity.Content;
import com.kartikay.streamhub.exception.ResourceNotFoundException;
import com.kartikay.streamhub.repository.ContentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentServiceTest {

    @Mock
    private ContentRepository contentRepository;

    @InjectMocks
    private ContentService contentService;

    @Test
    void getById_shouldReturnContent() {
        Content content = Content.builder().id(1L).title("Skybound").genre("Sci-Fi").build();
        when(contentRepository.findById(1L)).thenReturn(Optional.of(content));

        Content result = contentService.getById(1L);

        assertEquals("Skybound", result.getTitle());
    }

    @Test
    void getById_shouldThrowIfMissing() {
        when(contentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contentService.getById(1L));
    }

    @Test
    void create_shouldPersistContent() {
        ContentRequest request = new ContentRequest();
        request.setTitle("New Show");
        request.setGenre("Drama");
        request.setDescription("Desc");
        request.setReleaseYear(2024);
        request.setRating(8.1);

        when(contentRepository.save(any(Content.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Content saved = contentService.create(request);

        assertEquals("New Show", saved.getTitle());
        verify(contentRepository, times(1)).save(any(Content.class));
    }
}
