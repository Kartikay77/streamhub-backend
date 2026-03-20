package com.kartikay.streamhub.dto;

import com.kartikay.streamhub.entity.SubscriptionTier;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequest {
    @NotNull
    private SubscriptionTier tier;
}