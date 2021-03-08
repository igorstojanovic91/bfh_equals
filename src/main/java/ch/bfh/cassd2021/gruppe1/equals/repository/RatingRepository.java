package ch.bfh.cassd2021.gruppe1.equals.repository;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RatingRepository {

    final Logger logger = LoggerFactory.getLogger(RatingRepository.class);

    public void updateRatings(Rating[] ratings) {
        String updateQuery = "UPDATE Rating SET successRate=?, Version=?" +
                " WHERE studentId=? AND courseId=? AND version=?";

        Connection connection = null;

        try {
            connection = EqualsDataSource.getConnection();
            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
            connection.setAutoCommit(false);
            for (Rating rating : ratings) {
                updateStm.setInt(1, rating.getSuccessRate());
                updateStm.setInt(2, rating.getVersion() + 1);
                updateStm.setInt(3, rating.getStudentId());
                updateStm.setInt(4, rating.getCourseId());
                updateStm.setInt(5, rating.getVersion());
                updateStm.addBatch();
            }
            int nbrOfModifiedRows = updateStm.executeUpdate();
            if(nbrOfModifiedRows != ratings.length) {
                throw new SQLException("Not all rows affected");
            }
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
