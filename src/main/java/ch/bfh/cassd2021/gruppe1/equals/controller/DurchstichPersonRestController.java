package ch.bfh.cassd2021.gruppe1.equals.controller;

import ch.bfh.cassd2021.gruppe1.equals.business.DurchstichPersonRepository;
import ch.bfh.cassd2021.gruppe1.equals.business.Person;
import ch.bfh.cassd2021.gruppe1.equals.repository.DurchstichPersonRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/api/person/*")
public class DurchstichPersonRestController extends HttpServlet {
    private static final String JSON_MEDIA_TYPE = "application/json; ; charset=UTF-8";
    private static final String ROOT_PATH = "/";
    private static final String PERSONS_PATH = "/persons";

    private final Logger logger = LoggerFactory.getLogger(DurchstichPersonRestController.class);

    ObjectMapper jsonMapper;

    DurchstichPersonRepository personRepository;

    public DurchstichPersonRestController() {
        jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        personRepository = new DurchstichPersonRepositoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo() != null ? request.getPathInfo() : "/";

        if (path.matches(ROOT_PATH)) {
            logger.debug("Entering root path processing.");
            // do what?
        } else if (path.matches(PERSONS_PATH)) {
            logger.debug("Entering /persons path processing.");
            List<Person> allPersons = personRepository.getAllPersons();

            String allPersonsJson = jsonMapper.writeValueAsString(allPersons);
            response.setContentType(JSON_MEDIA_TYPE);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getOutputStream().print(allPersonsJson);
        }
    }
}

