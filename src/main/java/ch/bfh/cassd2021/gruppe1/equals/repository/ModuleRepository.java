package ch.bfh.cassd2021.gruppe1.equals.repository;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Course;
import ch.bfh.cassd2021.gruppe1.equals.business.model.CourseRating;
import ch.bfh.cassd2021.gruppe1.equals.business.model.Module;
import ch.bfh.cassd2021.gruppe1.equals.business.model.Rating;
import ch.bfh.cassd2021.gruppe1.equals.business.model.StudentCourseRating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModuleRepository {

    final Logger logger = LoggerFactory.getLogger(ModuleRepository.class);

    public List<Module> getModulesForPerson(int personId) {
        logger.debug("Entering getModulesForPerson()...");
        List<Module> moduleList = new ArrayList<>();

        String query = "SELECT DISTINCT m.id, m.name, m.shortName, m.startDate, m.endDate, m.headId, m.assistantId,"
            + " CASE"
            + " WHEN m.headId = ? THEN 'Head'"
            + " WHEN c.professorId = ? THEN 'Professor'"
            + " WHEN m.assistantId = ? THEN 'Assistant'"
            + " ELSE 'Student'"
            + " END AS role"
            + " FROM Module m"
            + " LEFT JOIN Course c ON m.id = c.moduleId"
            + " LEFT JOIN Registration r on m.id = r.moduleId"
            + " WHERE m.headId = ? OR c.professorId = ? OR m.assistantId = ? OR r.studentId = ?"
            + " ORDER BY m.shortName asc, m.startDate asc";

        try (Connection connection = EqualsDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, personId);
            statement.setInt(2, personId);
            statement.setInt(3, personId);
            statement.setInt(4, personId);
            statement.setInt(5, personId);
            statement.setInt(6, personId);
            statement.setInt(7, personId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Module module = new Module();
                module.setModuleId(resultSet.getInt("id"));
                module.setName(resultSet.getString("name"));
                module.setShortName(resultSet.getString("shortName"));
                module.setStartDate(resultSet.getDate("startDate"));
                module.setEndDate(resultSet.getDate("endDate"));
                module.setHeadId(resultSet.getInt("headId"));
                module.setAssistantId(resultSet.getInt("assistantId"));
                module.setRole(resultSet.getString("role"));
                moduleList.add(module);
            }

        } catch (SQLException throwables) {
            logger.error("Problem reading Database, message was {}", throwables.getMessage());
            throw new RepositoryException(throwables.getMessage());
        }

        return moduleList;
    }

    public List<StudentCourseRating> getSuccessRateOverviewForModule(int moduleId, int personId) {
        logger.debug("Entering getSuccessRateOverviewForModule()...");
        List<StudentCourseRating> studentCourseRatingList = new ArrayList<>();

        String query = "SELECT re.studentID, p.lastName, p.firstName,"
            + " c.id as courseId, c.name, c.shortName, c.weight,"
            + " ra.successRate, ra.version"
            + " FROM Registration re"
            + " INNER JOIN Person p ON re.studentId = p.id"
            + " INNER JOIN Course c ON re.moduleId = c.moduleId"
            + " LEFT JOIN Rating ra ON re.studentId = ra.studentId"
            + " LEFT JOIN Module m ON c.moduleId = m.id"
            + " WHERE re.moduleId = ?"
            + " AND (m.headId = ? OR m.assistantId = ? OR c.professorId = ? OR re.studentId = ?)"
            + " ORDER BY p.lastName asc, p.firstName asc, re.studentId asc, c.id asc";

        try (Connection connection = EqualsDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, moduleId);
            statement.setInt(2, personId);
            statement.setInt(3, personId);
            statement.setInt(4, personId);
            statement.setInt(5, personId);

            ResultSet resultSet = statement.executeQuery();
            int currentStudentId = 0;
            boolean isNewStudent = false;

            StudentCourseRating studentCourseRating = null;
            List<CourseRating> courseRatingList = null;

            while (resultSet.next()) {
                int studentId = resultSet.getInt("studentId");

                if (currentStudentId != studentId) {
                    currentStudentId = studentId;
                    isNewStudent = true;

                    studentCourseRating = new StudentCourseRating();
                    studentCourseRating.setStudentId(studentId);
                    studentCourseRating.setName(resultSet.getString("lastName") + " " + resultSet.getString("firstName"));

                    courseRatingList = new ArrayList<>();
                }

                Course course = new Course();
                course.setCourseId(resultSet.getInt("courseId"));
                course.setName(resultSet.getString("name"));
                course.setShortName(resultSet.getString("shortName"));
                course.setWeight(resultSet.getDouble("weight"));

                Rating rating = new Rating();
                rating.setStudentId(studentId);
                rating.setSuccessRate(resultSet.getInt("successRate"));
                rating.setVersion(resultSet.getInt("version"));

                CourseRating courseRating = new CourseRating(course, rating);
                courseRatingList.add(courseRating);

                studentCourseRating.setCourseRating(courseRatingList);

                studentCourseRating.calculatePreliminaryGrade();
                studentCourseRating.calculateOverallGrade();

                if (isNewStudent) {
                    studentCourseRatingList.add(studentCourseRating);
                    isNewStudent = false;
                }
            }

        } catch (SQLException throwables) {
            logger.error("Problem reading Database, message was {}", throwables.getMessage());
            throw new RepositoryException(throwables.getMessage());
        }

        return studentCourseRatingList;
    }

}
