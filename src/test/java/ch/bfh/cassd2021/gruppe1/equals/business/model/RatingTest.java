package ch.bfh.cassd2021.gruppe1.equals.business.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatingTest {

    @Test
    void setSuccessRate_0() {
        Rating rating = new Rating();
        rating.setSuccessRate(0);
        assertEquals(0, rating.getSuccessRate());
    }

    @Test
    void setSuccessRate_85() {
        Rating rating = new Rating();
        rating.setSuccessRate(85);
        assertEquals(85, rating.getSuccessRate());
    }

    @Test
    void setSuccessRate_100() {
        Rating rating = new Rating();
        rating.setSuccessRate(100);
        assertEquals(100, rating.getSuccessRate());
    }

    @Test
    void setSuccessRate_negative_nok() {
        Rating rating = new Rating();
        assertThrows(IllegalArgumentException.class, () -> {
            rating.setSuccessRate(-1);
        });
    }

    @Test
    void setSuccessRate_101_nok() {
        Rating rating = new Rating();
        assertThrows(IllegalArgumentException.class, () -> {
            rating.setSuccessRate(101);
        });
    }
}
