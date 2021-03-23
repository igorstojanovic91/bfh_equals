package ch.bfh.cassd2021.gruppe1.equals.controller;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Module;
import ch.bfh.cassd2021.gruppe1.equals.business.model.StudentCourseRating;
import ch.bfh.cassd2021.gruppe1.equals.business.service.ModuleService;
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
 * Returns a list of enrolled modules or a list of students of a module with the corresponding course grades via REST API.
 * Listens to "/api/modules/*" path.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
@WebServlet(urlPatterns = "/api/modules/*")
public class ModuleRestController extends HttpServlet {
    private static final String ACCEPT_TYPE = "application/json";
    private static final String JSON_MEDIA_TYPE = "application/json; charset=UTF-8";
    private static final String ROOT_PATH = "/";
    private static final String OVERALL_PATH = "/overall";

    private final Logger logger = LoggerFactory.getLogger(ModuleRestController.class);

    final ObjectMapper jsonMapper;

    final ModuleService moduleService;

    public ModuleRestController() {
        jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        moduleService = new ModuleService();
    }

    /**
     * For root path, a list of enrolled modules is returned.
     * For /overall path, a list students of a module with the corresponding course grades is returned.
     *
     * @param request  the http request
     * @param response the http response
     * @throws IOException is thrown if JSON-Object cannot be created
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo() != null ? request.getPathInfo() : "/";
        String acceptType = request.getHeader("Accept");

        if (!acceptType.equalsIgnoreCase(ACCEPT_TYPE)) {
            logger.warn("Wrong content type from request: " + acceptType);
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else {
            if (path.matches(ROOT_PATH)) {
                logger.debug("Entering /api/modules.");
                getModules(request, response);

            } else if (path.contains(OVERALL_PATH)) {
                logger.debug("Entering /api/modules/overall.");
                getStudentCourseRatings(request, response);

            } else {
                logger.debug("Path {} not implemented.", path);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void getModules(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int personId = (Integer) request.getAttribute("personId");
        List<Module> moduleList = moduleService.getModulesForPerson(personId);

        if (moduleList != null && moduleList.size() > 0) {
            response.setContentType(JSON_MEDIA_TYPE);
            response.setStatus(HttpServletResponse.SC_OK);
            logger.info("Modules found");

            JsonGenerator generator = jsonMapper
                .createGenerator(response.getOutputStream(), JsonEncoding.UTF8);
            generator.writeObject(moduleList);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            logger.warn("No modules found");
        }
    }

    private void getStudentCourseRatings(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int personId = (Integer) request.getAttribute("personId");
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty() && pathInfo.split("/").length > 2) {
            int moduleId = Integer.parseInt(pathInfo.split("/")[2]);
            List<StudentCourseRating> studentCourseRatingList = moduleService.getSuccessRateOverviewForModule(moduleId, personId);

            if (studentCourseRatingList != null && studentCourseRatingList.size() > 0) {
                response.setContentType(JSON_MEDIA_TYPE);
                response.setStatus(HttpServletResponse.SC_OK);
                logger.info("Student Course Ratings found");

                JsonGenerator generator = jsonMapper
                    .createGenerator(response.getOutputStream(), JsonEncoding.UTF8);
                generator.writeObject(studentCourseRatingList);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                logger.warn("No Student Course Ratings found: " + pathInfo);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
