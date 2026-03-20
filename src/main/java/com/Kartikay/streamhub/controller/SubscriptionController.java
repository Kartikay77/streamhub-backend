package com.kartikay.streamhub.controller;

import com.kartikay.streamhub.dto.ApiResponse;
import com.kartikay.streamhub.dto.SubscriptionRequest;
import com.kartikay.streamhub.entity.Subscription;
import com.kartikay.streamhub.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public Subscription createOrUpgrade(@Valid @RequestBody SubscriptionRequest request,
                                        Authentication authentication) {
        return subscriptionService.createOrUpgrade(authentication.getName(), request);
    }

    @DeleteMapping
    public ApiResponse cancel(Authentication authentication) {
        return subscriptionService.cancel(authentication.getName());
    }

    @GetMapping
    public List<Subscription> getMySubscriptions(Authentication authentication) {
        return subscriptionService.getUserSubscriptions(authentication.getName());
    }
}