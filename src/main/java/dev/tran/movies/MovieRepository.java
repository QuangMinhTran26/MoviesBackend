package dev.tran.movies;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends MongoRepository<Movie, ObjectId> { //actually talks to the database
    Optional<Movie> findMoviesByImdbId(String imdbId);
}
