package ch.bfh.cassd2021.gruppe1.equals.business.model;

import java.text.DecimalFormat;
import java.util.List;

public class StudentCourseRating {

    private int studentId;
    private String name;
    private List<CourseRating> courseRating;
    private int preliminaryGrade;
    private int overallGrade;

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

    public double getOverallGrade() {
        return overallGrade;
    }

    public void calculateGrades() {
        double sumOfOverallWeight = 0;
        double overallGrades = 0;
        double preliminaryGrades = 0;
        double sumOfPreliminaryWeight = 0;
        for(CourseRating courseRatings: courseRating){
            sumOfOverallWeight += courseRatings.getCourse().getWeight();
            overallGrades += courseRatings.getCourse().getWeight() * (courseRatings.getRating().getSuccessRate());
            if(courseRatings.getRating().getSuccessRate() > 0){
                sumOfPreliminaryWeight += courseRatings.getCourse().getWeight();
                preliminaryGrades += courseRatings.getCourse().getWeight() * (courseRatings.getRating().getSuccessRate());
            }
        }
        this.overallGrade = (int) Math.round(overallGrades/sumOfOverallWeight);
        this.preliminaryGrade = (int) Math.round(preliminaryGrades/sumOfPreliminaryWeight);
    }
}
