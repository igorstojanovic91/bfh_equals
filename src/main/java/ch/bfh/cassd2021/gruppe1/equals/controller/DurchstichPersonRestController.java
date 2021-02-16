package ch.bfh.cassd2021.gruppe1.equals.controller;


import ch.bfh.cassd2021.gruppe1.equals.business.model.Person;
import ch.bfh.cassd2021.gruppe1.equals.repository.DurchstichPersonRepository;
import ch.bfh.cassd2021.gruppe1.equals.repository.PersonShortDTO;
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
import java.util.List;

@WebServlet(urlPatterns = "/api/person/*")
public class DurchstichPersonRestController extends HttpServlet {

  private static final String JSON_MEDIA_TYPE = "application/json; charset=UTF-8";
  private static final String ROOT_PATH = "/";
  private static final String PERSONS_PATH = "/persons";
  private static final String PERSONS_SHORT_PATH = "/persons/short";

  private final Logger logger = LoggerFactory.getLogger(DurchstichPersonRestController.class);

  ObjectMapper jsonMapper;

  DurchstichPersonRepository personRepository;

  public DurchstichPersonRestController() {
    jsonMapper = new ObjectMapper();
    jsonMapper.registerModule(new JavaTimeModule());
    personRepository = new DurchstichPersonRepository();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String path = request.getPathInfo() != null ? request.getPathInfo() : "/";

    if (path.matches(ROOT_PATH)) {
      logger.debug("Entering root path processing.");
      // do what?

    } else if (path.matches(PERSONS_PATH)) {
      logger.debug("Entering /persons path processing.");
      List<Person> allPersons = personRepository.getAllPersons();

      response.setContentType(JSON_MEDIA_TYPE);
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpServletResponse.SC_OK);

      JsonGenerator generator = jsonMapper
          .createGenerator(response.getOutputStream(), JsonEncoding.UTF8);

      generator.writeObject(allPersons);

    } else if (path.matches(PERSONS_SHORT_PATH)) {
      logger.debug("Entering /persons/short path processing.");
      List<PersonShortDTO> allPersonsShort = personRepository.getAllPersonsShort();

      response.setContentType(JSON_MEDIA_TYPE);
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpServletResponse.SC_OK);

      JsonGenerator generator = jsonMapper
          .createGenerator(response.getOutputStream(), JsonEncoding.UTF8);

      generator.writeObject(allPersonsShort);

    } else {
      logger.debug("path {} not implemented.", path);
      String error = "Path " + path.toString() + " not implemented.";

      response.setContentType(JSON_MEDIA_TYPE);
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

      JsonGenerator generator = jsonMapper
          .createGenerator(response.getOutputStream(), JsonEncoding.UTF8);

      generator.writeObject(error);
    }

  }
}

