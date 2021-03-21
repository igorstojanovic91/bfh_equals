package ch.bfh.cassd2021.gruppe1.equals.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Class for accessing DB to check authentication
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class AuthenticationRepository {

    final Logger logger = LoggerFactory.getLogger(AuthenticationRepository.class);

    /**
     * Authenticates a user.
     * Returns the corresponding personId if authentication is successful.
     * eturns -1 otherwise.
     *
     * @param username the username
     * @param password the password
     * @return id of authenticated person
     */
    public int authenticateUser(String username, String password) {
        logger.debug("Entering authenticateUser()...");
        int personId = -1;

        try (Connection connection = EqualsDataSource.getConnection()) {
            String query = "SELECT id FROM Person WHERE userName = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setInt(2, password.hashCode());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                personId = resultSet.getInt("id");
            }

        } catch (SQLException throwables) {
            logger.error("Problem reading Database, message was {}", throwables.getMessage());
            throw new RepositoryException(throwables.getMessage());
        }
        return personId;
    }

    /**
     * Checks whether a person is either head of module or professor in a given course
     *
     * @param courseId the courseId
     * @param personId the personID
     * @return true if person is head of module or professor in course
     */
    public boolean isAuthorized(int courseId, int personId) {
        String query = "SELECT c.id FROM Course c" +
            " INNER JOIN Module m on c.moduleId = m.id" +
            " WHERE c.id = ? AND (c.professorId = ? OR m.headId = ?)";

        try (Connection connection = EqualsDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, courseId);
            statement.setInt(2, personId);
            statement.setInt(3, personId);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();

        } catch (SQLException throwables) {
            logger.error("Problem reading Database, message was {}", throwables.getMessage());
            throw new RepositoryException(throwables.getMessage());
        }

    }

    public boolean isStudent(int courseId, int studentId) {
        String query = "SELECT c.id FROM Course c"
                +" INNER JOIN Module m ON m.id = c.moduleId"
                +" INNER JOIN Registration r ON r.moduleId = m.id"
                +" WHERE c.id = ? AND r.studentId = ?;";

        try (Connection connection = EqualsDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, courseId);
            statement.setInt(2, studentId);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();

        } catch (SQLException throwables) {
            logger.error("Problem reading Database, message was {}", throwables.getMessage());
            throw new RepositoryException(throwables.getMessage());
        }

    }
}
