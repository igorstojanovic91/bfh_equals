package ch.bfh.cassd2021.gruppe1.equals.business.model;

public class Rating {
    private Person student;
    // private Course course;
    private int successRate;
    private int version;

    public Rating(Person student, int successRate, int version) {
        this.student = student;
        this.successRate = successRate;
        this.version = version;
    }

    public Person getStudent() {
        return student;
    }

    public void setStudent(Person student) {
        this.student = student;
    }

    /**
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    */

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Rating{" +
            "student=" + student +
            ", successRate=" + successRate +
            ", version=" + version +
            '}';
    }
}
