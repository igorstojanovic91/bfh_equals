package ch.bfh.cassd2021.gruppe1.equals.business.service;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Person;
import ch.bfh.cassd2021.gruppe1.equals.repository.PersonRepository;

public class PersonService {
    final PersonRepository personRepository;

    public PersonService() {
        personRepository = new PersonRepository();
    }

    public Person getPerson(int personId) {
        return personRepository.getPerson(personId);
    }
}
