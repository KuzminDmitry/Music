package com.gehtsoft.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehtsoft.threadPool.ThreadPoolSingleton;
import com.gehtsoft.token.Token;
import com.gehtsoft.token.TokenMemorySingleton;
import com.gehtsoft.core.User;
import com.gehtsoft.factory.SecurityFactory;
import com.gehtsoft.factory.ServiceFactory;
import com.gehtsoft.iDAO.IUserService;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Created by dkuzmin on 7/6/2016.
 */
@Path("security")
@Produces("application/json; charset=utf-8")
public class SecurityResource {

    final static Logger logger = Logger.getLogger("authenticate");

    @Context
    ServletContext servletContext;

    @Context
    HttpServletResponse response;

    @Context
    HttpServletRequest request;

    @Context
    public void checkCookies(HttpServletRequest httpServletRequest) throws Exception {
        this.request = httpServletRequest;
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            logger.error("Client cookies are not enabled!");
            response.sendError(400);
        }
    }

    @Path("/login")
    @POST
    public Response authenticateCredentials(@HeaderParam("username") String username, @HeaderParam("password") String password) throws Exception {
        logger.info("Authenticate user started.");
        if (username == null) {
            logger.error("Username is null!");
            return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode()).build();
        }
        if (password == null) {
            logger.error("Password is null!");
            return Response.status(Response.Status.PRECONDITION_FAILED.getStatusCode()).build();
        }
        String passwordHash = SecurityFactory.getPasswordHash().hash(password);

        User user = new User();
        user.setUserName(username);
        user.setPassword(passwordHash);

        user = (User) ThreadPoolSingleton.getInstance().userThread(User.class, "getByFilter", user);

        if (user == null) {
            logger.error("User with username=" + username + " and password=" + password + " not found!");
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).build();
        }

        logger.info("User found: " + user);

        Token token = TokenMemorySingleton.getInstance().addToken(user);

        if (token == null) {
            logger.error("Token for " + user + " not created!");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }

        logger.info("Generated token: " + token);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(token);
        return Response.status(Response.Status.OK.getStatusCode()).entity(json).build();
    }

    @Path("/logout")
    @POST
    public Response logout(@HeaderParam("jwt") String jwt) throws Exception {
        if (jwt != null) {
            Token token = TokenMemorySingleton.getInstance().getToken(jwt);
            if (token != null) {
                TokenMemorySingleton.getInstance().deleteToken(token);
            } else {
                logger.error("Token is null! User tried logout with his jwt, but token was not found!");
            }
        } else {
            logger.error("User tried logout with his jwt, but jwt is null!");
        }
        return Response.status(Response.Status.OK.getStatusCode()).build();
    }
}

