package ch.bfh.cassd2021.gruppe1.equals.business.service;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Person;
import ch.bfh.cassd2021.gruppe1.equals.repository.PersonRepository;
/**
 * The PersonService class implements the service layer for persons
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class PersonService {
    final PersonRepository personRepository;

    public PersonService() {
        personRepository = new PersonRepository();
    }

    /**
     * Returns a Person if the personId is found
     * @param personId the personId
     * @return a Person
     */
    public Person getPerson(int personId) {
        return personRepository.getPerson(personId);
    }
}
