package ch.bfh.cassd2021.gruppe1.equals.controller;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * The CorsFilter class implements the CORS-Filter.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
@WebFilter(urlPatterns = "/api/*")
public class CorsFilter extends HttpFilter {

    /**
     * Implements the cross-origin resource sharing.
     *
     * @param request  the http request
     * @param response the http response
     * @param chain    the filter chain
     * @throws IOException      is thrown when filtering on chain went wrong
     * @throws ServletException is thrown when filtering on chain went wrong
     */
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String origin = request.getHeader("Origin");
        response.addHeader("Access-Control-Allow-Origin", origin == null ? "*" : origin);
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Accept, Content-Type");
        chain.doFilter(request, response);
    }
}
