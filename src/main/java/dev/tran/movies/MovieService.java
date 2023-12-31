package dev.tran.movies;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository; //uses the repo class, talks to database and get the list of the movies and return to API layer

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> allMovies(){ //reference of MovieRepository

        return movieRepository.findAll(); //return all the movies
    }

    public Optional<Movie> singleMovie(String imdbId){
        return movieRepository.findMoviesByImdbId(imdbId);
    }
}
