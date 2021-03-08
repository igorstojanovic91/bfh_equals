package ch.bfh.cassd2021.gruppe1.equals.repository;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RatingRepository {

    final Logger logger = LoggerFactory.getLogger(RatingRepository.class);

    Connection connection = null;

    public void updateRatings(Rating[] ratings) {

        String updateQuery = "UPDATE Rating SET successRate=?, version=?" +
          " WHERE studentId=? AND courseId=? AND version=?";


        try {
            logger.debug("ENTERING DB QUEUE");
            connection = EqualsDataSource.getConnection();
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            connection.setAutoCommit(false);
            for (Rating rating : ratings) {
                // INPUT VALIDATION FIRST
                if(rating.getSuccessRate() < 0 || rating.getSuccessRate() > 100) {
                    throw  new SQLException("wrong value");
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
            logger.debug("nbr of modifed rows ");
            logger.debug(String.valueOf(nbrOfModifiedRows.length));
            logger.debug("ratings length ");
            logger.debug(String.valueOf(ratings.length));

            if(nbrOfModifiedRows.length != ratings.length) {
                throw new SQLException("Not all rows affected");
            }
            connection.commit();
            connection.setAutoCommit(true);
        }

        catch (SQLException sqlException) {
            try {
                connection.rollback(); // ROLL BACK IF NOT ALL ROWS ARE UPDATED
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }


    public void insertRatings(Rating[] ratings) {
        String insertQuery = "INSERT INTO Rating VALUES (?, ?, ?, ?)";

        try {
            connection = EqualsDataSource.getConnection();
            PreparedStatement updateStm = connection.prepareStatement(insertQuery);
            connection.setAutoCommit(false);
            for (Rating rating : ratings) {
                // INPUT VALIDATION FIRST
                if(rating.getSuccessRate() < 0 || rating.getSuccessRate() > 100) {
                    throw  new SQLException("wrong value");
                }

                updateStm.setInt(1, rating.getStudentId());
                updateStm.setInt(2, rating.getCourseId());
                updateStm.setInt(3, rating.getSuccessRate());
                updateStm.setInt(4, rating.getVersion() + 1);
                updateStm.addBatch();
                logger.debug("Added batch ");
            }
            int[] nbrOfModifiedRows = updateStm.executeBatch();

            logger.debug("nbr of modifed rows ");
            logger.debug(String.valueOf(nbrOfModifiedRows.length));
            logger.debug("ratings length ");
            logger.debug(String.valueOf(ratings.length));

            if(nbrOfModifiedRows.length != ratings.length) {
                throw new SQLException("Not all rows affected");
            }
            connection.commit();
            connection.setAutoCommit(true);
        }

        catch (SQLException sqlException) {
            try {
                connection.rollback(); // ROLL BACK IF NOT ALL ROWS ARE UPDATED
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

}
