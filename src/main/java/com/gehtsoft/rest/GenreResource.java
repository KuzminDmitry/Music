package com.gehtsoft.rest;

import com.gehtsoft.auth.AuthenticateChecker;
import com.gehtsoft.core.Genre;
import com.gehtsoft.threadPool.ThreadPoolSingleton;
import org.apache.log4j.Logger;

import javax.servlet.*;
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

    private boolean isAuthorized = true;

    @Context
    ServletContext servletContext;

    @Context
    HttpServletResponse response;

    @Context
    HttpServletRequest request;

    @Context
    public void checkAuthenticate(HttpServletRequest httpServletRequest) throws Exception {
        if (!AuthenticateChecker.validate(this.request)) {
            response.sendError(403);
            isAuthorized = false;
        }
    }

    @GET
    public List<Genre> getAllGenres() throws Exception {
        if(!isAuthorized) return null;
        logger.info("Get all genres started.");
        return (List) ThreadPoolSingleton.getInstance().basicThread(Genre.class, "getAll", null);

    }

    @GET
    @Path("{id}")
    public Genre getGenreById(@NotNull @PathParam("id") Integer id) throws Exception {
        if(!isAuthorized) return null;
        logger.info("Search started for genre by parameter: " + " id " + id + ".");
        return (Genre) ThreadPoolSingleton.getInstance().basicThread(Genre.class, "getById", id);
    }

    @PUT
    public Genre updateGenre(Genre genre) throws Exception {
        if(!isAuthorized) return null;
        logger.info("Update started for genre by singer track: " + genre + ".");
        return (Genre) ThreadPoolSingleton.getInstance().basicThread(Genre.class, "update", genre);
    }

    @DELETE
    public void deleteGenre(@NotNull @QueryParam("id") Integer id) throws Exception {
        if(!isAuthorized) return;
        logger.info("Delete started for genre by parameter: " + " id " + id + ".");
        ThreadPoolSingleton.getInstance().basicThread(Genre.class, "deleteById", id);
    }

    @POST
    public Genre insertGenre(Genre genre) throws Exception {
        if(!isAuthorized) return null;
        logger.info("Add new genre started: " + genre + ".");
        return (Genre) ThreadPoolSingleton.getInstance().basicThread(Genre.class, "add", genre);
    }
}

