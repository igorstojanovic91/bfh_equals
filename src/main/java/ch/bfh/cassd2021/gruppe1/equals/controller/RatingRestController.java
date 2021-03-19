package ch.bfh.cassd2021.gruppe1.equals.controller;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Rating;
import ch.bfh.cassd2021.gruppe1.equals.business.service.RatingService;
import ch.bfh.cassd2021.gruppe1.equals.repository.AuthenticationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/api/ratings")
public class RatingRestController extends HttpServlet {
    private static final String JSON_MEDIA_TYPE = "application/json; charset=UTF-8";
    private final Logger logger = LoggerFactory.getLogger(RatingRestController.class);

    final ObjectMapper jsonMapper;
    final RatingService ratingService;
    final AuthenticationRepository authenticationRepository;

    public RatingRestController() {
        jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        ratingService = new RatingService();
        authenticationRepository = new AuthenticationRepository();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Entering /api/ratings");

        String contentType = request.getContentType();
        if (contentType.matches(JSON_MEDIA_TYPE)) {

            try {
                String body = request.getReader()
                    .lines()
                    .reduce("", (String::concat));
                Rating[] ratings = jsonMapper.readValue(body, Rating[].class);

                if (isAuthorized(ratings, request)) {
                    ratingService.updateRatings(ratings);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            } catch (Exception e) {
                logger.debug("Content does not match Rating object");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        } else {
            logger.debug("Media type not accepted");
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Entering /api/ratings");

        String contentType = request.getContentType();
        if (contentType.matches(JSON_MEDIA_TYPE)) {

            try {
                String body = request.getReader()
                    .lines()
                    .reduce("", (String::concat));
                Rating[] ratings = jsonMapper.readValue(body, Rating[].class);

                if (isAuthorized(ratings, request)) {
                    ratingService.insertRatings(ratings);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }

            } catch (Exception exception) {
                logger.debug("Content does not match Rating object");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            logger.debug("Media type not accepted");
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }
    }

    private boolean isAuthorized(Rating[] ratings, HttpServletRequest request) {
        int personId = (Integer) request.getAttribute("personId");
        boolean userIsAuthorized = true;
        for (Rating rating : ratings) {
            if (!authenticationRepository.isAuthorized(rating.getCourseId(), personId)) userIsAuthorized = false;
        }
        return userIsAuthorized;
    }

}
