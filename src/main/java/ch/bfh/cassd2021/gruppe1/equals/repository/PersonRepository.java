package ch.bfh.cassd2021.gruppe1.equals.repository;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Class for accessing DB to get Person.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class PersonRepository {

    final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

    /**
     * Gets a Person if the personId is found in the database.
     *
     * @param personId the personId
     * @return a Person object
     */
    public Person getPerson(int personId) {
        logger.debug("Entering PersonRepository.getPerson()...");
        Person person = null;

        try (Connection connection = EqualsDataSource.getConnection()) {
            String query = "SELECT * FROM Person WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, personId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
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
            }

        } catch (SQLException throwables) {
            logger.error("Problem reading Database, message was {}", throwables.getMessage());
            throw new RepositoryException(throwables.getMessage());
        }
        return person;
    }
}
