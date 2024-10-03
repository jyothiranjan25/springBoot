package com.example.springboot.MovieApp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "movies", indexes = {
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_genre", columnList = "genre")
},uniqueConstraints = {
        @UniqueConstraint(name = "uk_title", columnNames = "title")
})
@EntityListeners(MovieListener.class)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;

    private String description;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    private String genre;

    @Column(name = "image_poster_path")
    private String imagePosterPath;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
