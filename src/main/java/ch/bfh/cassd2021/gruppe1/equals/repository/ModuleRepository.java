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
            + " ORDER BY m.endDate desc, m.shortName asc";

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

        String query = "SELECT DISTINCT re.studentID, p.lastName, p.firstName, c.id as courseId, c.name, c.shortName, c.moduleId, c.professorId, c.weight, ra.successRate, ra.version"
            + " FROM Registration re"
            + " INNER JOIN Module m on re.moduleId = m.id"
            + " INNER JOIN Course c on c.moduleId = m.id"
            + " LEFT JOIN Rating ra on ra.courseId = c.id AND re.studentId = ra.studentId"
            + " INNER JOIN Person p on p.id = re.studentId"
            + " WHERE m.id = ?"
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

            fillStudentCourseRatingList(studentCourseRatingList, resultSet);

        } catch (SQLException throwables) {
            logger.error("Problem reading Database, message was {}", throwables.getMessage());
            throw new RepositoryException(throwables.getMessage());
        }

        return studentCourseRatingList;
    }

    public List<Integer> getModulesWithoutGrades(int personId) {
        logger.debug("Entering getModulesWithoutGrades()...");
        List<Integer> integerList = new ArrayList<>();

        String query = "SELECT DISTINCT m.id"
            + " FROM Module m"
            + " LEFT JOIN Course c ON m.id = c.moduleId"
            + " LEFT JOIN Registration r on m.id = r.moduleId"
            + " LEFT JOIN Rating ra on r.studentId = ra.studentId AND c.id = ra.courseId"
            + " WHERE (m.headId = ? OR c.professorId = ? OR m.assistantId = ?)"
            + " AND (ra.successRate is NULL OR ra.successRate = 0)";

        try (Connection connection = EqualsDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, personId);
            statement.setInt(2, personId);
            statement.setInt(3, personId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                integerList.add(resultSet.getInt("id"));
            }

        } catch (SQLException throwables) {
            logger.error("Problem reading Database, message was {}", throwables.getMessage());
            throw new RepositoryException(throwables.getMessage());
        }
        return integerList;
    }

    private void fillStudentCourseRatingList(List<StudentCourseRating> studentCourseRatingList, ResultSet resultSet) throws SQLException {
        int currentStudentId = 0;
        boolean isNewStudent = false;

        StudentCourseRating studentCourseRating = new StudentCourseRating();
        List<CourseRating> courseRatingList = new ArrayList<>();

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
            course.setModuleId(resultSet.getInt("moduleId"));
            course.setProfessorId(resultSet.getInt("professorId"));
            course.setWeight(resultSet.getDouble("weight"));

            Rating rating = new Rating();
            rating.setStudentId(studentId);
            rating.setCourseId(resultSet.getInt("courseId"));
            rating.setSuccessRate(resultSet.getInt("successRate"));
            rating.setVersion(resultSet.getInt("version"));

            CourseRating courseRating = new CourseRating(course, rating);
            courseRatingList.add(courseRating);

            studentCourseRating.setCourseRating(courseRatingList);

            if (isNewStudent) {
                studentCourseRatingList.add(studentCourseRating);
                isNewStudent = false;
            }
        }
    }
}
