package com.kartikay.streamhub.service;

import com.kartikay.streamhub.dto.ApiResponse;
import com.kartikay.streamhub.dto.FavoriteRequest;
import com.kartikay.streamhub.dto.WatchHistoryRequest;
import com.kartikay.streamhub.entity.*;
import com.kartikay.streamhub.exception.ResourceNotFoundException;
import com.kartikay.streamhub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final FavoriteRepository favoriteRepository;
    private final WatchHistoryRepository watchHistoryRepository;

    public ApiResponse addFavorite(String email, FavoriteRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Content content = contentRepository.findById(request.getContentId())
                .orElseThrow(() -> new ResourceNotFoundException("Content not found"));

        if (favoriteRepository.findByUserIdAndContentId(user.getId(), content.getId()).isPresent()) {
            return new ApiResponse("Already in favorites");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .content(content)
                .build();
        favoriteRepository.save(favorite);
        return new ApiResponse("Added to favorites");
    }

    public List<Favorite> getFavorites(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return favoriteRepository.findByUserId(user.getId());
    }

    public ApiResponse addWatchHistory(String email, WatchHistoryRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Content content = contentRepository.findById(request.getContentId())
                .orElseThrow(() -> new ResourceNotFoundException("Content not found"));

        WatchHistory watchHistory = WatchHistory.builder()
                .user(user)
                .content(content)
                .watchedMinutes(request.getWatchedMinutes() == null ? 0 : request.getWatchedMinutes())
                .watchedAt(LocalDateTime.now())
                .build();

        watchHistoryRepository.save(watchHistory);
        return new ApiResponse("Watch history added");
    }

    public List<WatchHistory> getWatchHistory(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return watchHistoryRepository.findByUserIdOrderByWatchedAtDesc(user.getId());
    }
}