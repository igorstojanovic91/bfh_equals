package ch.bfh.cassd2021.gruppe1.equals.controller;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Course;
import ch.bfh.cassd2021.gruppe1.equals.business.service.CourseService;
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
/**
 * Returns a courseList via REST API.
 * Listens to "/api/courses/*" path.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
@WebServlet(urlPatterns = "/api/courses/*")
public class CourseRestController extends HttpServlet {
    private static final String ACCEPT_TYPE = "application/json";
    private static final String JSON_MEDIA_TYPE = "application/json; charset=UTF-8";

    private final Logger logger = LoggerFactory.getLogger(CourseRestController.class);

    final ObjectMapper jsonMapper;

    final CourseService courseService;

    public CourseRestController() {
        jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        courseService = new CourseService();
    }

    /**
     * Get a courseList
     * @param request the http request
     * @param response the http response
     * @throws IOException is thrown when JSON-Object can't be created
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug("Entering /api/courses");
        String acceptType = request.getHeader("Accept");

        if (!acceptType.equalsIgnoreCase(ACCEPT_TYPE)) {
            logger.warn("Wrong content type from request: " + acceptType);
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else {
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && !pathInfo.isEmpty() && pathInfo.split("/").length > 1) {
                int moduleId = Integer.parseInt(pathInfo.split("/")[1]);
                List<Course> courseList = courseService.getCoursesForModule(moduleId, (Integer) request.getAttribute("personId"));

                if (courseList != null && courseList.size() > 0) {
                    response.setContentType(JSON_MEDIA_TYPE);
                    response.setStatus(HttpServletResponse.SC_OK);
                    logger.info("Courses found");

                    JsonGenerator generator = jsonMapper
                        .createGenerator(response.getOutputStream(), JsonEncoding.UTF8);
                    generator.writeObject(courseList);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    logger.warn("No courses found: " + pathInfo);
                }

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}
