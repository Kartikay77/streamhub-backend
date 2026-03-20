package com.kartikay.streamhub.service;

import com.kartikay.streamhub.dto.ApiResponse;
import com.kartikay.streamhub.dto.SubscriptionRequest;
import com.kartikay.streamhub.entity.*;
import com.kartikay.streamhub.exception.ResourceNotFoundException;
import com.kartikay.streamhub.repository.SubscriptionRepository;
import com.kartikay.streamhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public Subscription createOrUpgrade(String userEmail, SubscriptionRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Subscription subscription = subscriptionRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .orElse(null);

        if (subscription != null && subscription.getStatus() == SubscriptionStatus.ACTIVE) {
            subscription.setTier(request.getTier());
            return subscriptionRepository.save(subscription);
        }

        Subscription newSubscription = Subscription.builder()
                .user(user)
                .tier(request.getTier())
                .status(SubscriptionStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        return subscriptionRepository.save(newSubscription);
    }

    public ApiResponse cancel(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Subscription subscription = subscriptionRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No subscription found"));

        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscription.setCanceledAt(LocalDateTime.now());
        subscriptionRepository.save(subscription);

        return new ApiResponse("Subscription canceled");
    }

    public List<Subscription> getUserSubscriptions(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return subscriptionRepository.findByUserId(user.getId());
    }
}