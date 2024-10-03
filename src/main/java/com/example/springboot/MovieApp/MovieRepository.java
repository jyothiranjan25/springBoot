package com.example.springboot.MovieApp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Page<Movie> findAll(Pageable pageable);

    <Optional> Movie findByTitleIgnoreCase(String title);

    <Optional> Movie findByGenreIgnoreCase(String genre);

    <Optional> Movie findByRatingGreaterThanEqual(Double rating);
}
