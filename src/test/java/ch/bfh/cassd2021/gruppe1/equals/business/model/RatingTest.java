package ch.bfh.cassd2021.gruppe1.equals.business.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatingTest {

    private Rating rating;

    @BeforeEach
    void init() {
        rating = new Rating();

    }

    @Test
    void setSuccessRate_0() {
        rating.setSuccessRate(0);
        assertEquals(0, rating.getSuccessRate());
    }

    @Test
    void setSuccessRate_85() {
        rating.setSuccessRate(85);
        assertEquals(85, rating.getSuccessRate());
    }

    @Test
    void setSuccessRate_100() {
        rating.setSuccessRate(100);
        assertEquals(100, rating.getSuccessRate());
    }

    @Test
    void setSuccessRate_negative_nok() {
        assertThrows(IllegalArgumentException.class, () -> {
            rating.setSuccessRate(-1);
        });
    }

    @Test
    void setSuccessRate_101_nok() {
        assertThrows(IllegalArgumentException.class, () -> {
            rating.setSuccessRate(101);
        });
    }
}
