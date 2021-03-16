package ch.bfh.cassd2021.gruppe1.equals.business.model;

public class CourseRating {

    private Course course;
    private Rating rating;

    public CourseRating(Course course, Rating rating) {
        this.course = course;
        this.rating = rating;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public int getCourseSuccessRate(){
        return this.rating.getSuccessRate();
    }

    public double getCourseWeight(){
        return this.course.getWeight();
    }

}
