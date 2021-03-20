package ch.bfh.cassd2021.gruppe1.equals.repository;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Class for accessing DB to get courses for module.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class CourseRepository {

    final Logger logger = LoggerFactory.getLogger(CourseRepository.class);

    /**
     * Gets all courese of a module where the person has a role.
     * @param moduleId the moduleId
     * @param personId the personId
     * @return List of courses of the module
     */
    public List<Course> getCoursesForModule(int moduleId, int personId) {
        logger.debug("Entering getCoursesForModule()...");
        List<Course> courseList = new ArrayList<>();

        String query = "SELECT  DISTINCT c.id, c.name, c.shortName, c.moduleId, c.professorId, c.weight"
            + " FROM Course c"
            + " LEFT JOIN Module m ON c.moduleId = m.id"
            + " LEFT JOIN Registration r on m.id = r.moduleId"
            + " WHERE m.id = ?"
            + " AND (m.headId = ? OR m.assistantId = ? OR c.professorId = ? OR r.studentId = ?)"
            + " ORDER BY c.shortName asc";

        try (Connection connection = EqualsDataSource.getConnection()) {
            ResultSet resultSet = getResultSet(moduleId, personId, query, connection);

            while (resultSet.next()) {
                Course course = new Course();
                course.setCourseId(resultSet.getInt("id"));
                course.setName(resultSet.getString("name"));
                course.setShortName(resultSet.getString("shortName"));
                course.setModuleId(resultSet.getInt("moduleId"));
                course.setProfessorId(resultSet.getInt("professorId"));
                course.setWeight(resultSet.getDouble("weight"));
                courseList.add(course);
            }

        } catch (SQLException throwables) {
            logger.error("Problem reading Database, message was {}", throwables.getMessage());
            throw new RepositoryException(throwables.getMessage());
        }

        return courseList;
    }

    protected static ResultSet getResultSet(int moduleId, int personId, String query, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, moduleId);
        statement.setInt(2, personId);
        statement.setInt(3, personId);
        statement.setInt(4, personId);
        statement.setInt(5, personId);

        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }
}
