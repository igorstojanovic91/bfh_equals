package ch.bfh.cassd2021.gruppe1.equals.controller;

import ch.bfh.cassd2021.gruppe1.equals.repository.AuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@WebFilter(urlPatterns = "/api/*")
public class AuthenticationFilter extends HttpFilter {

    private static final String JSON_MEDIA_TYPE = "application/json; charset=UTF-8";
    private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    AuthenticationRepository authenticationRepository;

    public AuthenticationFilter() {
        authenticationRepository = new AuthenticationRepository();
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
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
                response.setContentType(JSON_MEDIA_TYPE);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                int personId = authenticationRepository.authenticateUser(credentials[0], credentials[1]);
                if (personId > -1) {
                    request.setAttribute("personId", personId);
                    chain.doFilter(request, response);
                } else {
                    response.setContentType(JSON_MEDIA_TYPE);
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
