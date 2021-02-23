package ch.bfh.cassd2021.gruppe1.equals.business.model;

import java.util.List;

public class StudentCourseRating {

    private int studentId;
    private String name;
    private List<CourseRating> courseRating;
    private double preliminaryGrade;
    private double overallGrade;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseRating> getCourseRating() {
        return courseRating;
    }

    public void setCourseRating(List<CourseRating> courseRating) {
        this.courseRating = courseRating;
    }

    public double getPreliminaryGrade() {
        return preliminaryGrade;
    }

    public void setPreliminaryGrade(double preliminaryGrade) {
        this.preliminaryGrade = preliminaryGrade;
    }

    public double getOverallGrade() {
        return overallGrade;
    }

    public void setOverallGrade(double overallGrade) {
        this.overallGrade = overallGrade;
    }

    // TODO Calculate Preliminary Grade
    public void calculatePreliminaryGrade() {
        this.preliminaryGrade = 0;
    }

    // TODO Calculate Overall Grade
    public void calculateOverallGrade() {
        this.overallGrade = 0;
    }
}
