package ch.bfh.cassd2021.gruppe1.equals.business;

import java.util.List;

public interface DurchstichPersonRepository {

    List<Person> getAllPersons() throws RepositoryException;
}
