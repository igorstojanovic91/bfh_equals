package ch.bfh.cassd2021.gruppe1.equals.business.service;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Rating;
import ch.bfh.cassd2021.gruppe1.equals.repository.RatingRepository;

/**
 * The RatingService class implements the service layer for ratings
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class RatingService {
    final RatingRepository ratingRepository;

    public RatingService() {
        ratingRepository = new RatingRepository();
    }

    /**
     * Updates ratings
     *
     * @param ratings an array of Rating objects
     */
    public void updateRatings(Rating[] ratings) {
        ratingRepository.updateRatings(ratings);
    }

    /**
     * Inserts ratings
     *
     * @param ratings an array of Rating objects
     */
    public void insertRatings(Rating[] ratings) {
        ratingRepository.insertRatings(ratings);
    }
}
