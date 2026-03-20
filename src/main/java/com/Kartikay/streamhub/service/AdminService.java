package com.kartikay.streamhub.service;

import com.kartikay.streamhub.repository.ContentRepository;
import com.kartikay.streamhub.repository.SubscriptionRepository;
import com.kartikay.streamhub.repository.UserRepository;
import com.kartikay.streamhub.repository.WatchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final WatchHistoryRepository watchHistoryRepository;

    public Map<String, Long> dashboard() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("users", userRepository.count());
        stats.put("contentItems", contentRepository.count());
        stats.put("subscriptions", subscriptionRepository.count());
        stats.put("watchEvents", watchHistoryRepository.count());
        return stats;
    }
}