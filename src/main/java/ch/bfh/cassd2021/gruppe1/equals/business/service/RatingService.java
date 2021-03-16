package ch.bfh.cassd2021.gruppe1.equals.business.service;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Rating;
import ch.bfh.cassd2021.gruppe1.equals.repository.RatingRepository;

public class RatingService {
    RatingRepository ratingRepository;

    public RatingService() {
        ratingRepository = new RatingRepository();
    }

    public void updateRatings(Rating[] ratings) {
        ratingRepository.updateRatings(ratings);
    }

    public void insertRatings(Rating[] ratings) {
        ratingRepository.insertRatings(ratings);
    }
}
