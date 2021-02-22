package ch.bfh.cassd2021.gruppe1.equals.business.model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private int courseId;
    private String name;
    private String shortName;
    private Person professor;
    private double weight;
    private List<Rating> ratingList = new ArrayList<>();

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    public Person getProfessor() {
        return professor;
    }

    public void setProfessor(Person professor) {
        this.professor = professor;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void addRating(Rating rating) {
        ratingList.add(rating);
    }

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    @Override
    public String toString() {
        return "Course{" +
            "courseId=" + courseId +
            ", name='" + name + '\'' +
            ", shortName='" + shortName + '\'' +
            ", professorId=" + professor +
            ", weight=" + weight +
            ", ratings=" + ratingList +
            '}';
    }
}
