package com.gehtsoft.rest;

import com.gehtsoft.token.Token;
import com.gehtsoft.token.TokenMemorySingleton;
import com.gehtsoft.core.Genre;
import com.gehtsoft.threadPool.ThreadPoolSingleton;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.List;

/**
 * Created by dkuzmin on 7/6/2016.
 */
@Path("genre")
@Produces("application/json; charset=utf-8")
public class GenreResource {

    final static Logger logger = Logger.getLogger("resource");

    private boolean error = false;

    @Context
    ServletContext servletContext;

    @Context
    HttpServletResponse response;

    @Context
    HttpServletRequest request;

    @Context
    public void checkAuthenticate(HttpServletRequest httpServletRequest) throws Exception {
        this.request = httpServletRequest;
        String jwt = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("authdata")) {
                    jwt = cookie.getValue();
                    logger.info("JWT from cookies: " + jwt);
                }
            }
        } else {
            logger.error("Cookies is null!");
            response.sendError(403);
            error = true;
        }
        if (jwt == null) {
            logger.error("JWT is null. User is not authorized. Access denied!");
            response.sendError(403);
            error = true;
        } else {
            Token token = TokenMemorySingleton.getInstance().getToken(jwt);
            if (token == null) {
                logger.error("Token is null. User is not authorized. Access denied!");
                response.sendError(403);
                error = true;
            }
            else {
                logger.info("User is authorized. Access is allowed!");
            }
        }
    }


    @GET
    public List<Genre> getAllGenres() throws Exception {
        if(!error) {
            logger.info("Get all genres started.");
            return (List) ThreadPoolSingleton.getInstance().basicThread(Genre.class, "getAll", null);
        }
        return null;
    }

    @GET
    @Path("{id}")
    public Genre getGenreById(@NotNull @PathParam("id") Integer id) throws Exception {
        if(!error) {
            logger.info("Search started for genre by parameter: " + " id " + id + ".");
            return (Genre) ThreadPoolSingleton.getInstance().basicThread(Genre.class, "getById", id);
        }
        return null;
    }

    @PUT
    public Genre updateGenre(Genre genre) throws Exception {
        if(!error) {
            logger.info("Update started for genre by singer track: " + genre + ".");
            return (Genre) ThreadPoolSingleton.getInstance().basicThread(Genre.class, "update", genre);
        }
        return null;
    }

    @DELETE
    public void deleteGenre(@NotNull @QueryParam("id") Integer id) throws Exception {
        if(!error) {
            logger.info("Delete started for genre by parameter: " + " id " + id + ".");
            ThreadPoolSingleton.getInstance().basicThread(Genre.class, "deleteById", id);
        }
    }

    @POST
    public Genre insertGenre(Genre genre) throws Exception {
        if(!error) {
            logger.info("Add new genre started: " + genre + ".");
            return (Genre) ThreadPoolSingleton.getInstance().basicThread(Genre.class, "add", genre);
        }
        return null;
    }
}

