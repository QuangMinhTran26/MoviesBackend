package dev.tran.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    public Review createReview(String reviewBody, String imdbId){ //associate the review with the movie
        Review review = reviewRepository.insert(new Review(reviewBody));

        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId)) // which movie is being updated
                .apply(new Update().push("reviewIds").value(review)) //using the template to perform an update call on the movie class, since each movie contains an empty array of review IDs
                .first();

        return review;
    }
}
