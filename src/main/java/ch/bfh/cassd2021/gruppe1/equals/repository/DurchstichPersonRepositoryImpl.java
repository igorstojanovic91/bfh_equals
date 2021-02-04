package ch.bfh.cassd2021.gruppe1.equals.repository;

import ch.bfh.cassd2021.gruppe1.equals.business.DurchstichPersonRepository;
import ch.bfh.cassd2021.gruppe1.equals.business.Person;
import ch.bfh.cassd2021.gruppe1.equals.business.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DurchstichPersonRepositoryImpl implements DurchstichPersonRepository {
    final Logger logger = LoggerFactory.getLogger(DurchstichPersonRepositoryImpl.class);

    @Override
    public List<Person> getAllPersons() {
        logger.debug("Entering getAllPersons()...");
        List<Person> allPersons = new ArrayList<>();

        // try-with-resources schliesst connection automatisch, auch bei Exception
        try (Connection connection = EqualsDataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Person;")) {

            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
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
            logger.error("Problem reading Database, mesage was {}", e.getMessage());
            throw new RepositoryException(e.getMessage());
        }


        return allPersons;
    }
}
