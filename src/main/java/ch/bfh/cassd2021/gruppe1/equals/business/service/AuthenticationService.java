package ch.bfh.cassd2021.gruppe1.equals.business.service;

import ch.bfh.cassd2021.gruppe1.equals.repository.AuthenticationRepository;

/**
 * The AuthenticationService class implements the service layer for authentication
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class AuthenticationService {
    final AuthenticationRepository authenticationRepository;

    public AuthenticationService() {
        authenticationRepository = new AuthenticationRepository();
    }

    /**
     * Returns the personId if credentials are valid
     * Returns -1 if credentials are invalid
     * @param username the username
     * @param password the password
     * @return personId
     */
    public int authenticateUser(String username, String password) {
        return authenticationRepository.authenticateUser(username, password);
    }
}
