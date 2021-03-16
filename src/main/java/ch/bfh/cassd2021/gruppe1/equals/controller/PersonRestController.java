package ch.bfh.cassd2021.gruppe1.equals.controller;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Person;
import ch.bfh.cassd2021.gruppe1.equals.business.service.PersonService;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/api/persons/*")
public class PersonRestController extends HttpServlet {
    private static final String JSON_MEDIA_TYPE = "application/json; charset=UTF-8";

    private final Logger logger = LoggerFactory.getLogger(PersonRestController.class);

    final ObjectMapper jsonMapper;

    final PersonService personService;

    public PersonRestController() {
        jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        personService = new PersonService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug("Entering /api/persons");
        Person person;
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            int personId = Integer.parseInt(pathInfo.split("/")[1]);
            person = personService.getPerson(personId);
        } else {
            person = personService.getPerson((Integer) request.getAttribute("personId"));
        }
        response.setContentType(JSON_MEDIA_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);

        JsonGenerator generator = jsonMapper
            .createGenerator(response.getOutputStream(), JsonEncoding.UTF8);

        generator.writeObject(person);
    }
}
