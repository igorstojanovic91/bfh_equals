package ch.bfh.cassd2021.gruppe1.equals.controller;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Course;
import ch.bfh.cassd2021.gruppe1.equals.business.model.Person;
import ch.bfh.cassd2021.gruppe1.equals.repository.AuthenticationRepository;
import ch.bfh.cassd2021.gruppe1.equals.repository.CourseRepository;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@WebServlet(urlPatterns = "/api/courses/*")
public class CourseRestController extends HttpServlet {
    private static final String JSON_MEDIA_TYPE = "application/json; charset=UTF-8";
    private static final String ROOT_PATH = "/";
    private static final String COURSES_PATH = "/courses";

    private final Logger logger = LoggerFactory.getLogger(CourseRestController.class);

    ObjectMapper jsonMapper;

    CourseRepository courseRepository;
    AuthenticationRepository authenticationRepository;

    public CourseRestController() {
        jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        courseRepository = new CourseRepository();
        authenticationRepository = new AuthenticationRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String[] tokens = header.split(" ");
        if (!tokens[0].equals("Basic")) {
            throw new IllegalArgumentException();
        }
        byte[] decoded = Base64.getDecoder().decode(tokens[1]);
        String[] credentials = new String(decoded).split(":");
        if (credentials.length != 2) {
            response.setContentType(JSON_MEDIA_TYPE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            Person person = authenticationRepository.authenticateUser(credentials[0], Integer.parseInt(credentials[1]));
            if (person != null) {
                String pathInfo = request.getPathInfo();
                if (pathInfo != null && !pathInfo.isEmpty()) {
                    int moduleId = Integer.parseInt(pathInfo.split("/")[1]);
                    List<Course> courseList = courseRepository.getCoursesForModule(moduleId, person.getPersonId());

                    response.setContentType(JSON_MEDIA_TYPE);
                    response.setStatus(HttpServletResponse.SC_OK);

                    JsonGenerator generator = jsonMapper
                        .createGenerator(response.getOutputStream(), JsonEncoding.UTF8);

                    generator.writeObject(courseList);
                } else {
                    response.setContentType(JSON_MEDIA_TYPE);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }

            } else {
                response.setContentType(JSON_MEDIA_TYPE);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }
}
