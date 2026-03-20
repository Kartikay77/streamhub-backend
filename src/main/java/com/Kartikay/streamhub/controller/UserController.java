package com.kartikay.streamhub.controller;

import com.kartikay.streamhub.dto.*;
import com.kartikay.streamhub.entity.Favorite;
import com.kartikay.streamhub.entity.WatchHistory;
import com.kartikay.streamhub.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/favorites")
    public ApiResponse addFavorite(@Valid @RequestBody FavoriteRequest request, Authentication authentication) {
        return userService.addFavorite(authentication.getName(), request);
    }

    @GetMapping("/favorites")
    public List<Favorite> favorites(Authentication authentication) {
        return userService.getFavorites(authentication.getName());
    }

    @PostMapping("/watch-history")
    public ApiResponse addWatchHistory(@Valid @RequestBody WatchHistoryRequest request,
                                       Authentication authentication) {
        return userService.addWatchHistory(authentication.getName(), request);
    }

    @GetMapping("/watch-history")
    public List<WatchHistory> history(Authentication authentication) {
        return userService.getWatchHistory(authentication.getName());
    }
}