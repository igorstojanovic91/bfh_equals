package ch.bfh.cassd2021.gruppe1.equals.controller;

import ch.bfh.cassd2021.gruppe1.equals.business.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;


/**
 * Basic Authentication Filter for REST API.
 * Listens to "/api/*" path.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
@WebFilter(urlPatterns = "/api/*")
public class AuthenticationFilter extends HttpFilter {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    final AuthenticationService authenticationService;

    public AuthenticationFilter() {
        authenticationService = new AuthenticationService();
    }

    /**
     * Authenticates a user with Basic Authentication.
     * Checks whether the request is valid and if it contains a valid authentication header.
     *
     * @param request  the http request
     * @param response the http response
     * @param chain    the filter chain
     */
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        try {
            logger.debug("Entering Authentication Filter.");
            String header = request.getHeader("Authorization");
            String[] tokens = header.split(" ");
            if (!tokens[0].equals("Basic")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            byte[] decoded = Base64.getDecoder().decode(tokens[1]);
            String[] credentials = new String(decoded).split(":");
            if (credentials.length != 2) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                int personId = authenticationService.authenticateUser(credentials[0], credentials[1]);
                if (personId > -1) {
                    request.setAttribute("personId", personId);
                    chain.doFilter(request, response);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    logger.warn("Unauthorized!");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
