package ch.bfh.cassd2021.gruppe1.equals.controller;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Rating;
import ch.bfh.cassd2021.gruppe1.equals.business.service.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Adds or updates ratings via REST API.
 * Listens to "/api/ratings" path.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
@WebServlet(urlPatterns = "/api/ratings")
public class RatingRestController extends HttpServlet {
    private static final String JSON_MEDIA_TYPE = "application/json";
    private final Logger logger = LoggerFactory.getLogger(RatingRestController.class);

    final ObjectMapper jsonMapper;
    final RatingService ratingService;

    public RatingRestController() {
        jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        ratingService = new RatingService();
    }

    /**
     * Updates ratings.
     *
     * @param request  the http request
     * @param response the http response
     */
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
                    logger.debug("User not allowed to modify ratings");
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

    /**
     * Adds ratings.
     *
     * @param request  the http request
     * @param response the http response
     */
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
                    logger.debug("User not allowed to set ratings");
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
        return ratingService.isAuthorized(personId, ratings);
    }

}
