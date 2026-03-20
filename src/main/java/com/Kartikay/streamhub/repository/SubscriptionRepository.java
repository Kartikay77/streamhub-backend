package com.kartikay.streamhub.repository;

import com.kartikay.streamhub.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserId(Long userId);
    Optional<Subscription> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}