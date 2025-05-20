package com.example.cinema_ticket_booking_system.daos;

import com.example.cinema_ticket_booking_system.models.MovieModel;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovieModelDao {
    
    MovieModel save(MovieModel movie);
    
    boolean update(MovieModel movie);
    
    boolean delete(int id);
    
    Optional<MovieModel> findById(int id);
    
    List<MovieModel> findByTitle(String title);
    
    List<MovieModel> findByGenre(String genre);
    
    List<MovieModel> findByReleaseDateAfter(LocalDate date);
    
    List<MovieModel> findAll();
    
    List<MovieModel> findCurrentMovies();
    
    List<MovieModel> findUpcomingMovies();
}
