package ch.bfh.cassd2021.gruppe1.equals.repository;


import ch.bfh.cassd2021.gruppe1.equals.business.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DurchstichPersonRepository {

    final Logger logger = LoggerFactory.getLogger(DurchstichPersonRepository.class);

    public List<Person> getAllPersons() {
        logger.debug("Entering getAllPersons()...");
        List<Person> allPersons = new ArrayList<>();

        // try-with-resources schliesst connection automatisch, auch bei Exception
        try (Connection connection = EqualsDataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Person;")) {

            while (resultSet.next()) {
                Person person = new Person();
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
                allPersons.add(person);
            }

        } catch (SQLException e) {
            logger.error("Problem reading Database, message was {}", e.getMessage());
            throw new RepositoryException(e.getMessage());
        }

        return allPersons;
    }

    public List<PersonShortDTO> getAllPersonsShort() {
        logger.debug("Entering getAllPersonsShort()...");
        List<PersonShortDTO> allPersonsShort = new ArrayList<>();

        // try-with-resources schliesst connection automatisch, auch bei Exception
        try (Connection connection = EqualsDataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement
                 .executeQuery("SELECT id, firstName, lastName FROM Person;")) {

            while (resultSet.next()) {
                PersonShortDTO.Builder personBuilder = new PersonShortDTO.Builder(resultSet.getInt("id"));
                String name = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
                personBuilder.withName(name);

                allPersonsShort.add(personBuilder.build());
            }

        } catch (SQLException e) {
            logger.error("Problem reading Database, message was {}", e.getMessage());
            throw new RepositoryException(e.getMessage());
        }

        return allPersonsShort;
    }
}
