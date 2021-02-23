package ch.bfh.cassd2021.gruppe1.equals.repository;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class AuthenticationRepository {

    final Logger logger = LoggerFactory.getLogger(AuthenticationRepository.class);

    public Person authenticateUser(String username, int password){
        logger.debug("Entering authenticateUser()...");
        Person person = null;

        try (Connection connection = EqualsDataSource.getConnection()){
            String query = "SELECT * FROM Person WHERE userName = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setInt(2, password);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                person = new Person();
                person.setPersonId(resultSet.getInt("id"));
                person.setLastName(resultSet.getString("lastName"));
                person.setFirstName(resultSet.getString("firstName"));

                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                if (dateOfBirth != null) {
                    person.setDateOfBirth(dateOfBirth.toLocalDate());
                }

                person.setPlaceOfOrigin(resultSet.getString("placeOfOrigin"));
                person.setSex(resultSet.getString("sex"));
                person.setUserName(resultSet.getString("userName"));
                person.setPassword(resultSet.getInt("password"));
            }

        } catch (SQLException throwables) {
            logger.error("Problem reading Database, mesage was {}", throwables.getMessage());
            throw new RepositoryException(throwables.getMessage());
        }
        return person;
    }
}
