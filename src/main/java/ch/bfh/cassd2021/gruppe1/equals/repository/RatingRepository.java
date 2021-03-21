package ch.bfh.cassd2021.gruppe1.equals.repository;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.IntStream;


/**
 * Class for accessing DB to update or add Ratings
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class RatingRepository {

    final Logger logger = LoggerFactory.getLogger(RatingRepository.class);

    Connection connection = null;

    /**
     * Updates ratings if input is valid
     *
     * @param ratings an array of Rating objects
     */
    public void updateRatings(Rating[] ratings) {

        String updateQuery = "UPDATE Rating SET successRate=?, version=?"
            + " WHERE studentId=? AND courseId=? AND version=?";

        try {
            logger.debug("ENTERING DB QUEUE");
            connection = EqualsDataSource.getConnection();
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            connection.setAutoCommit(false);
            for (Rating rating : ratings) {
                // INPUT VALIDATION FIRST
                if (rating.getSuccessRate() < 0 || rating.getSuccessRate() > 100) {
                    throw new SQLException("wrong value");
                }

                updateStm.setInt(1, rating.getSuccessRate());
                updateStm.setInt(2, rating.getVersion() + 1);
                updateStm.setInt(3, rating.getStudentId());
                updateStm.setInt(4, rating.getCourseId());
                updateStm.setInt(5, rating.getVersion());
                updateStm.addBatch();
                logger.debug("Added batch ");
            }
            int[] nbrOfModifiedRows = updateStm.executeBatch();

            if (IntStream.of(nbrOfModifiedRows).sum() != ratings.length) {
                throw new SQLException("Not all rows affected");
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException sqlException) {
            try {
                connection.rollback(); // ROLL BACK IF NOT ALL ROWS ARE UPDATED
                connection.setAutoCommit(true);
                throw new RepositoryException("Data not saved to Db");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * Inserts ratings if input is valid
     *
     * @param ratings an array of Rating objects
     */
    public void insertRatings(Rating[] ratings) {
        String insertQuery = "INSERT INTO Rating VALUES (?, ?, ?, ?)";

        try {
            connection = EqualsDataSource.getConnection();
            PreparedStatement updateStm = connection.prepareStatement(insertQuery);
            connection.setAutoCommit(false);
            for (Rating rating : ratings) {
                // INPUT VALIDATION FIRST
                if (rating.getSuccessRate() < 0 || rating.getSuccessRate() > 100) {
                    throw new SQLException("wrong value");
                }

                updateStm.setInt(1, rating.getStudentId());
                updateStm.setInt(2, rating.getCourseId());
                updateStm.setInt(3, rating.getSuccessRate());
                updateStm.setInt(4, rating.getVersion() + 1);
                updateStm.addBatch();
                logger.debug("Added batch ");
            }
            int[] nbrOfModifiedRows = updateStm.executeBatch();

            if (IntStream.of(nbrOfModifiedRows).sum() != ratings.length) {
                throw new SQLException("Not all rows affected");
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException sqlException) {
            try {
                connection.rollback(); // ROLL BACK IF NOT ALL ROWS ARE UPDATED
                connection.setAutoCommit(true);
                throw new RepositoryException("Data not saved to Db");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
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

    /**
     * Checks whether a student is enrolled in a given course
     *
     * @param courseId  the courseId
     * @param studentId the studentId
     * @return true if student is enrolled in course, false otherwise
     */
    public boolean isStudent(int courseId, int studentId) {
        String query = "SELECT c.id FROM Course c"
            + " INNER JOIN Module m ON m.id = c.moduleId"
            + " INNER JOIN Registration r ON r.moduleId = m.id"
            + " WHERE c.id = ? AND r.studentId = ?;";

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
