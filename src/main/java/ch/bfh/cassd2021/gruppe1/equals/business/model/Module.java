package ch.bfh.cassd2021.gruppe1.equals.business.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Module {
    private int moduleId;
    private String name;
    private String shortName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Person head;
    private Person assistant;
    private List<Course> courses;
    private List<Person> students;

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Person> getStudents() {
        return students;
    }

    public void setStudents(List<Person> students) {
        this.students = students;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate.toLocalDate();
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate.toLocalDate();
    }

    public Person getHead() {
        return head;
    }

    public void setHead(Person head) {
        if(head.getRole() == Role.HEAD) {
            this.head = head;
        }
        // TODO THROW ERROR HERE?
    }

    public Person getAssistant() {
        return assistant;
    }

    public void setAssistant(Person assistant) {
        if(assistant.getRole() == Role.ASSISTANT) {
            this.assistant = assistant;
        }
        // TODO THROW ERROR HERE?
    }

    //TODO CHECK FOR LOGIC -> NOT YET RIGHT
    public Map<Person, Integer> calcFinalGrade() {
        Map<Person, Integer> gradeMap = new HashMap<>();

        for(Person p : students) {
            int grade = 0;
            for(Course course: courses) {
                List<Rating> ratingList = course.getRatingList();
                for(Rating rating : ratingList) {
                    if(rating.getStudent().getPersonId() == p.getPersonId()) {
                        grade += course.getWeight() / 10 * rating.getSuccessRate();
                    }
                }
            }
            gradeMap.put(p, grade);
        }
        return gradeMap;
    }

    //TODO CHECK FOR LOGIC -> NOT YET RIGHT
    public Map<Person, Integer> calcPrimilinaryGrade() {
        Map<Person, Integer> gradeMap = new HashMap<>();
        for(Person p : students) {
            int grade = 0;
            int weight = 0;
            for(Course course: courses) {
                List<Rating> ratingList = course.getRatingList();
                for(Rating rating : ratingList) {
                    if(rating.getStudent().getPersonId() == p.getPersonId()) {
                        weight += course.getWeight();
                        grade += course.getWeight() * rating.getSuccessRate();
                    }
                }
            }
            grade = grade / weight;
            gradeMap.put(p, grade);
        }
        return gradeMap;
    }



    @Override
    public String toString() {
        return "Module{" +
            "moduleId=" + moduleId +
            ", name='" + name + '\n' +
            ", shortName='" + shortName + '\n' +
            ", startDate=" + startDate + '\n' +
            ", endDate=" + endDate + '\n' +
            ", head = " + head + '\n' +
            ", assistant = " + assistant + '\n' +
            ", students = " + students + '\n' +
            '}';
    }
}
