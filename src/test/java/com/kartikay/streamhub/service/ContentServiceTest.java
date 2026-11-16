
package com.kartikay.streamhub.service;

import com.kartikay.streamhub.dto.ContentRequest;
import com.kartikay.streamhub.entity.Content;
import com.kartikay.streamhub.exception.ResourceNotFoundException;
import com.kartikay.streamhub.repository.ContentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentServiceTest {

    @Mock
    private ContentRepository contentRepository;

    @InjectMocks
    private ContentService contentService;

    // Existing test 1
    @Test
    void getById_shouldReturnContent() {
        Content content = Content.builder()
                .id(1L)
                .title("Skybound")
                .genre("Sci-Fi")
                .build();

        when(contentRepository.findById(1L))
                .thenReturn(Optional.of(content));

        Content result = contentService.getById(1L);

        assertEquals("Skybound", result.getTitle());
    }

    // Existing test 2
    @Test
    void getById_shouldThrowIfMissing() {
        when(contentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> contentService.getById(1L)
        );
    }

    // Existing test 3
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
        verify(contentRepository, times(1))
                .save(any(Content.class));
    }

    // New test 4: First page retrieval
    @Test
    void getAll_shouldReturnFirstPage() {
        Pageable pageable = PageRequest.of(0, 2);

        List<Content> contents = List.of(
                Content.builder().id(1L).title("Kitchen Wars").build(),
                Content.builder().id(2L).title("Signal Zero").build()
        );

        Page<Content> expectedPage =
                new PageImpl<>(contents, pageable, 3);

        when(contentRepository.findAll(pageable))
                .thenReturn(expectedPage);

        Page<Content> result = contentService.getAll(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals(0, result.getNumber());
        assertTrue(result.isFirst());
        assertFalse(result.isLast());
    }

    // New test 5: Second page retrieval
    @Test
    void getAll_shouldReturnSecondPage() {
        Pageable pageable = PageRequest.of(1, 2);

        List<Content> contents = List.of(
                Content.builder().id(3L).title("Skybound").build()
        );

        Page<Content> expectedPage =
                new PageImpl<>(contents, pageable, 3);

        when(contentRepository.findAll(pageable))
                .thenReturn(expectedPage);

        Page<Content> result = contentService.getAll(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getNumber());
        assertEquals(3, result.getTotalElements());
        assertTrue(result.isLast());
    }

    // New test 6: Sorting parameters
    @Test
    void getAll_shouldSupportSorting() {
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by("title").ascending()
        );

        List<Content> contents = List.of(
                Content.builder().id(1L).title("Kitchen Wars").build(),
                Content.builder().id(2L).title("Signal Zero").build()
        );

        when(contentRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(contents, pageable, 2));

        Page<Content> result = contentService.getAll(pageable);

        assertEquals("Kitchen Wars",
                result.getContent().get(0).getTitle());

        assertTrue(result.getSort().isSorted());

        verify(contentRepository).findAll(pageable);
    }

    // New test 7: Empty results
    @Test
    void getAll_shouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);

        when(contentRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(
                        Collections.emptyList(),
                        pageable,
                        0
                ));

        Page<Content> result = contentService.getAll(pageable);

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }

    // New test 8: Repository called exactly once
    @Test
    void getAll_shouldCallRepositoryOnce() {
        Pageable pageable = PageRequest.of(0, 5);

        when(contentRepository.findAll(pageable))
                .thenReturn(Page.empty(pageable));

        contentService.getAll(pageable);

        verify(contentRepository, times(1))
                .findAll(pageable);

        verifyNoMoreInteractions(contentRepository);
    }
}
