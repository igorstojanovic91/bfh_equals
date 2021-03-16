package ch.bfh.cassd2021.gruppe1.equals.business.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentCourseRatingTest {

    @Test
    void calculateGrades_ok() {
        double[] weights = {1.8, 1.8, 1.6, 1.6, 1.6, 1.6};
        int[] grades = {85, 85, 85, 85, 85, 85};
        StudentCourseRating studentCourseRating = mockTestStudentCourseRating(weights, grades);

        studentCourseRating.calculateGrades();
        assertEquals(85, studentCourseRating.getPreliminaryGrade());
        assertEquals(85, studentCourseRating.getOverallGrade());
    }

    @Test
    void calculateGrades_32() {
        double[] weights = {1.8, 1.8, 1.6, 1.6, 1.6, 1.6};
        int[] grades = {33, 25, 25, 5, 20, 82};
        StudentCourseRating studentCourseRating = mockTestStudentCourseRating(weights, grades);

        studentCourseRating.calculateGrades();
        assertEquals(32, studentCourseRating.getPreliminaryGrade());
        assertEquals(32, studentCourseRating.getOverallGrade());
    }

    @Test
    void calculateGrades_0() {
        double[] weights = {1.8, 1.8, 1.6, 1.6, 1.6, 1.6};
        int[] grades = {0, 0, 0, 0, 0, 0};
        StudentCourseRating studentCourseRating = mockTestStudentCourseRating(weights, grades);

        studentCourseRating.calculateGrades();
        assertEquals(0, studentCourseRating.getPreliminaryGrade());
        assertEquals(0, studentCourseRating.getOverallGrade());
    }

    @Test
    void calculateGrades_100() {
        double[] weights = {1.8, 1.8, 1.6, 1.6, 1.6, 1.6};
        int[] grades = {100, 100, 100, 100, 100, 100};
        StudentCourseRating studentCourseRating = mockTestStudentCourseRating(weights, grades);

        studentCourseRating.calculateGrades();
        assertEquals(100, studentCourseRating.getPreliminaryGrade());
        assertEquals(100, studentCourseRating.getOverallGrade());
    }

    @Test
    void calculateGrades_81_53() {
        double[] weights = {1.8, 1.8, 1.6, 1.6, 1.6, 1.6};
        int[] grades = {74, 0, 0, 85, 77, 89};
        StudentCourseRating studentCourseRating = mockTestStudentCourseRating(weights, grades);

        studentCourseRating.calculateGrades();
        assertEquals(81, studentCourseRating.getPreliminaryGrade());
        assertEquals(53, studentCourseRating.getOverallGrade());
    }

    @Test
    void calculateGrades_nok() {
        double[] weights = {1.8, 1.8, 1.6, 1.6, 1.6, 1.6};
        int[] grades = {74, 0, 0, 85, 77, 89};
        StudentCourseRating studentCourseRating = mockTestStudentCourseRating(weights, grades);

        studentCourseRating.calculateGrades();
        assertEquals(81, studentCourseRating.getPreliminaryGrade());
        assertEquals(53, studentCourseRating.getOverallGrade());
    }

    private StudentCourseRating mockTestStudentCourseRating(double[] weights, int[] grades) {
        StudentCourseRating studentCourseRating = new StudentCourseRating();
        List<CourseRating> courseRatingList = new ArrayList<>();

        int i = 0;
        while (i < grades.length) {
            CourseRating courseRating = mock(CourseRating.class);
            when(courseRating.getCourseWeight()).thenReturn(weights[i]);
            when(courseRating.getCourseSuccessRate()).thenReturn(grades[i]);
            courseRatingList.add(courseRating);
            i++;
        }

        studentCourseRating.setCourseRating(courseRatingList);

        return studentCourseRating;
    }
}
