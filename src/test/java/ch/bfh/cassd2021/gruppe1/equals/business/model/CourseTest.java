package ch.bfh.cassd2021.gruppe1.equals.business.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void setWeight_0_1() {
        Course course = new Course();
        course.setWeight(0.1);
        assertEquals(0.1, course.getWeight());
    }

    @Test
    void setWeight_1_6() {
        Course course = new Course();
        course.setWeight(1.6);
        assertEquals(1.6, course.getWeight());
    }

    @Test
    void setWeight_nok() {
        Course course = new Course();
        assertThrows(IllegalArgumentException.class, () -> {
            course.setWeight(-1);
        });
    }
}
