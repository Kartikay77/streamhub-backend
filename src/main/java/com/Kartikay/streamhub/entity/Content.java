package com.kartikay.streamhub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "content")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;

    @Column(length = 1000)
    private String description;

    private Integer releaseYear;
    private Double rating;
}