package ch.bfh.cassd2021.gruppe1.equals.business.model;

/**
 * The Rating class implements a rating where a student's success rate in a course is stored
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class Rating {
    private int studentId;
    private int courseId;
    private int successRate;
    private int version;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        if (successRate >= 0 && successRate <= 100) {
            this.successRate = successRate;
        } else {
            throw new IllegalArgumentException();
        }
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
            "studentId=" + studentId +
            ", courseId=" + courseId +
            ", successRate=" + successRate +
            ", version=" + version +
            '}';
    }
}
