package ch.bfh.cassd2021.gruppe1.equals.business.model;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        Person head = new Person();
        head.setPersonId(1);
        head.setFirstName("Beatrice");
        head.setLastName("Amrhen");
        head.setRole(Role.HEAD);

        Person assistant = new Person();
        assistant.setPersonId(2);
        assistant.setFirstName("test");
        assistant.setLastName("Moser");
        assistant.setRole(Role.ASSISTANT);

        Person prof = new Person();
        prof.setPersonId(3);
        prof.setFirstName("Stephan");
        prof.setLastName("Fischli");
        prof.setRole(Role.PROFESSOR);

        Course java = new Course();
        java.setName("Java");
        java.setWeight(2.5);
        java.setProfessor(prof);

        Course webapp = new Course();
        webapp.setName("Webapp");
        webapp.setWeight(2.5);
        webapp.setProfessor(prof);

        List<Course> courses = new ArrayList<>();
        courses.add(webapp);
        courses.add(java);

        Person student = new Person();
        student.setFirstName("Igor");
        student.setLastName("Stojanovic");
        student.setRole(Role.STUDENT);

        Rating ratingIgor = new Rating(student, 82, 1 );
        java.addRating(ratingIgor);

        List<Person> students = new ArrayList<>();
        students.add(student);

        Module sd = new Module();
        sd.setName("Software Development");
        sd.setHead(head);
        sd.setAssistant(assistant);
        sd.setCourses(courses);
        sd.setStudents(students);

        System.out.println(sd);
        System.out.println(sd.getCourses());

        //TODO => How we want to incoportae the ratings
        //does each course store a list of Rating objects?


    }
}
