package ch.bfh.cassd2021.gruppe1.equals.controller;

import ch.bfh.cassd2021.gruppe1.equals.business.model.Rating;
import ch.bfh.cassd2021.gruppe1.equals.repository.RatingRepository;
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
import java.sql.SQLException;
import java.util.Arrays;

@WebServlet(urlPatterns = "/api/ratings")
public class RatingRestController extends HttpServlet {
    private static final String JSON_MEDIA_TYPE = "application/json; charset=UTF-8";
    private final Logger logger = LoggerFactory.getLogger(RatingRestController.class);

    ObjectMapper jsonMapper;
    RatingRepository ratingRepository;

    public RatingRestController() {
        jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        ratingRepository = new RatingRepository();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Entering /api/ratings");

        //SECURITY WISE WE NED TO KNOW HERE IF IT IS A PROFESSOR OR HEAD
        String contentType = request.getContentType();
        if(contentType.matches(JSON_MEDIA_TYPE)) {

            try {
                String body = request.getReader()
                        .lines()
                        .reduce("", (String::concat));
                Rating[] ratings = jsonMapper.readValue(body, Rating[].class);

                ratingRepository.updateRatings(ratings);

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

        //SECURITY WISE WE NED TO KNOW HERE IF IT IS A PROFESSOR OR HEAD
        String contentType = request.getContentType();
        if(contentType.matches(JSON_MEDIA_TYPE)) {

            try {
                String body = request.getReader()
                        .lines()
                        .reduce("", (String::concat));
                Rating[] ratings = jsonMapper.readValue(body, Rating[].class);

                ratingRepository.insertRatings(ratings);

            //    response.setStatus(HttpServletResponse.SC_NO_CONTENT);

           // } catch(SQLException sqlException){

            }
            catch (Exception e) {
                logger.debug("Content does not match Rating object");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        } else {
            logger.debug("Media tye not accepted");
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

    }
}
