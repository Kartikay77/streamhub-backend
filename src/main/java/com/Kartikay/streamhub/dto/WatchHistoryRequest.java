package com.kartikay.streamhub.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WatchHistoryRequest {
    @NotNull
    private Long contentId;

    private Integer watchedMinutes;
}