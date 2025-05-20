package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.MovieModel;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for MovieModel
 * Provides methods to interact with movie data in the database
 */
public interface MovieModelDao {
    
    /**
     * Save a new movie to the database
     * 
     * @param movie the movie to save
     * @return the saved movie with generated ID
     */
    MovieModel save(MovieModel movie);
    
    /**
     * Update an existing movie in the database
     * 
     * @param movie the movie to update
     * @return true if successful, false otherwise
     */
    boolean update(MovieModel movie);
    
    /**
     * Delete a movie from the database by ID
     * 
     * @param id the ID of the movie to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int id);
    
    /**
     * Find a movie by ID
     * 
     * @param id the ID of the movie to find
     * @return Optional containing the movie if found, empty optional otherwise
     */
    Optional<MovieModel> findById(int id);
    
    /**
     * Find movies by title (can be partial match)
     * 
     * @param title full or partial movie title
     * @return List of movies matching the title pattern
     */
    List<MovieModel> findByTitle(String title);
    
    /**
     * Find movies by genre
     * 
     * @param genre the genre to filter by
     * @return List of movies with the specified genre
     */
    List<MovieModel> findByGenre(String genre);
    
    /**
     * Find movies released after a specific date
     * 
     * @param date the release date to filter by
     * @return List of movies released after the specified date
     */
    List<MovieModel> findByReleaseDateAfter(LocalDate date);
    
    /**
     * Find all movies in the database
     * 
     * @return List of all movies
     */
    List<MovieModel> findAll();
    
    /**
     * Find current movies (released within last 3 months)
     * 
     * @return List of current movies
     */
    List<MovieModel> findCurrentMovies();
    
    /**
     * Find upcoming movies (release date in the future)
     * 
     * @return List of upcoming movies
     */
    List<MovieModel> findUpcomingMovies();
}
