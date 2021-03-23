package ch.bfh.cassd2021.gruppe1.equals.business.service;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Course;
import ch.bfh.cassd2021.gruppe1.equals.repository.CourseRepository;

import java.util.List;

/**
 * The CourseService class implements the service layer for courses.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class CourseService {
    final CourseRepository courseRepository;

    public CourseService() {
        courseRepository = new CourseRepository();
    }

    /**
     * Returns a list of courses for a specific module and person.
     *
     * @param moduleId the moduleId
     * @param personId the personId
     * @return a list of Courses
     */
    public List<Course> getCoursesForModule(int moduleId, int personId) {
        return courseRepository.getCoursesForModule(moduleId, personId);
    }
}
