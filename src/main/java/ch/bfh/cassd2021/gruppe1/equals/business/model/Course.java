package ch.bfh.cassd2021.gruppe1.equals.business.model;

/**
 * The Course class implements a course of a module.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class Course {
    private int courseId;
    private String name;
    private String shortName;
    private int moduleId;
    private int professorId;
    private double weight;

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

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight > 0) {
            this.weight = weight;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return "Course{" +
            "courseId=" + courseId +
            ", name='" + name + '\'' +
            ", shortName='" + shortName + '\'' +
            ", moduleId=" + moduleId +
            ", professorId=" + professorId +
            ", weight=" + weight +
            '}';
    }
}
