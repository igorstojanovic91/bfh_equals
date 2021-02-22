package ch.bfh.cassd2021.gruppe1.equals.business.model;

import ch.bfh.cassd2021.gruppe1.equals.repository.ModuleOverviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        Rating ratingIgor2 = new Rating(student, 20, 1 );
        webapp.addRating(ratingIgor2);

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

        // TEST WITH RDB
        ModuleOverviewRepository rep = new ModuleOverviewRepository();

        List<Module> moduleList = rep.getModules(1);

        System.out.println(moduleList);
        // TEST GRADE CALCULATION
        Map<Person, Integer> gradeTest = sd.calcFinalGrade();

        for (Map.Entry<Person, Integer> entry : gradeTest.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
        }

        Map<Person, Integer> gradePriminilary = sd.calcPrimilinaryGrade();

        for (Map.Entry<Person, Integer> entry : gradePriminilary.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
        }

    }
}
