package com.kartikay.streamhub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContentRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String genre;

    private String description;
    private Integer releaseYear;
    private Double rating;
}