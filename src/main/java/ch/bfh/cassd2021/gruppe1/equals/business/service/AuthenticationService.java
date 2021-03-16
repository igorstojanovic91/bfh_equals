package ch.bfh.cassd2021.gruppe1.equals.business.service;

import ch.bfh.cassd2021.gruppe1.equals.repository.AuthenticationRepository;

public class AuthenticationService {
    AuthenticationRepository authenticationRepository;

    public AuthenticationService() {
        authenticationRepository = new AuthenticationRepository();
    }

    public int authenticateUser(String username, String password) {
        return authenticationRepository.authenticateUser(username, password);
    }
}
