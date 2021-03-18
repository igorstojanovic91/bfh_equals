package ch.bfh.cassd2021.gruppe1.equals.business.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    private Course course;

    @BeforeEach
    void init() {
        course = new Course();
    }


    @Test
    void setWeight_0_1() {
        course.setWeight(0.1);
        assertEquals(0.1, course.getWeight());
    }

    @Test
    void setWeight_1_6() {
        course.setWeight(1.6);
        assertEquals(1.6, course.getWeight());
    }

    @Test
    void setWeight_nok() {
        assertThrows(IllegalArgumentException.class, () -> {
            course.setWeight(-1);
        });
    }
}
