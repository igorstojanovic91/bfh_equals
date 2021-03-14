package ch.bfh.cassd2021.gruppe1.equals.business.service;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Course;
import ch.bfh.cassd2021.gruppe1.equals.repository.CourseRepository;

import java.util.List;

public class CourseService {
    CourseRepository courseRepository;

    public CourseService() {
        courseRepository = new CourseRepository();
    }

    public List<Course> getCoursesForModule(int moduleId, int personId) {
        return courseRepository.getCoursesForModule(moduleId, personId);
    }
}
