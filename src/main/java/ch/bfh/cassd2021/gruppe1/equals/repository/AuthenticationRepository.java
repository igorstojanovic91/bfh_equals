package ch.bfh.cassd2021.gruppe1.equals.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationRepository {

    final Logger logger = LoggerFactory.getLogger(AuthenticationRepository.class);

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
}
