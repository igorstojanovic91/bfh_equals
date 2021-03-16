package ch.bfh.cassd2021.gruppe1.equals.controller;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Rating;
import ch.bfh.cassd2021.gruppe1.equals.business.service.RatingService;
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

@WebServlet(urlPatterns = "/api/ratings")
public class RatingRestController extends HttpServlet {
    private static final String JSON_MEDIA_TYPE = "application/json; charset=UTF-8";
    private final Logger logger = LoggerFactory.getLogger(RatingRestController.class);

    ObjectMapper jsonMapper;
    RatingService ratingService;

    public RatingRestController() {
        jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        ratingService = new RatingService();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Entering /api/ratings");

        //SECURITY WISE WE NEED TO KNOW HERE IF IT IS A PROFESSOR OR HEAD
        String contentType = request.getContentType();
        if (contentType.matches(JSON_MEDIA_TYPE)) {

            try {
                String body = request.getReader()
                    .lines()
                    .reduce("", (String::concat));
                Rating[] ratings = jsonMapper.readValue(body, Rating[].class);

                ratingService.updateRatings(ratings);

                response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            } catch (Exception e) {
                logger.debug("Content does not match Rating object");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        } else {
            logger.debug("Media tye not accepted");
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Entering /api/ratings");

        //SECURITY WISE WE NEED TO KNOW HERE IF IT IS A PROFESSOR OR HEAD
        String contentType = request.getContentType();
        if (contentType.matches(JSON_MEDIA_TYPE)) {

            try {
                String body = request.getReader()
                    .lines()
                    .reduce("", (String::concat));
                Rating[] ratings = jsonMapper.readValue(body, Rating[].class);

                ratingService.insertRatings(ratings);

                response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            } catch (Exception exception) {
                logger.debug("Content does not match Rating object");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            logger.debug("Media tye not accepted");
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

    }
}
