package com.kartikay.streamhub.repository;

import com.kartikay.streamhub.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByGenreIgnoreCase(String genre);
    List<Content> findByTitleContainingIgnoreCase(String keyword);
}