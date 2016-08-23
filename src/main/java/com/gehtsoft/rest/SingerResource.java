package com.gehtsoft.rest;

import com.gehtsoft.auth.AuthenticateChecker;
import com.gehtsoft.token.Token;
import com.gehtsoft.token.TokenMemorySingleton;
import com.gehtsoft.core.Singer;
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
@Path("singer")
@Produces("application/json; charset=utf-8")
public class SingerResource {

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
        this.response = AuthenticateChecker.validate(this.request, this.response);
    }

    @GET
    public List<Singer> getAllSingers() throws Exception {
        if(!error) {
            logger.info("Get all singers started.");
            return (List) ThreadPoolSingleton.getInstance().basicThread(Singer.class, "getAll", null);
        }
        return null;
    }

    @GET
    @Path("{id}")
    public Singer getSingerById(@NotNull @PathParam("id") Integer id) throws Exception {
        if(!error) {
            logger.info("Search started for singer by parameter: " + " id " + id + ".");
            return (Singer) ThreadPoolSingleton.getInstance().basicThread(Singer.class, "getById", id);
        }
        return null;
    }

    @PUT
    public Singer updateSinger(Singer singer) throws Exception {
        if(!error) {
            logger.info("Update started for singer by singer track: " + singer + ".");
            return (Singer) ThreadPoolSingleton.getInstance().basicThread(Singer.class, "update", singer);
        }
        return null;
    }

    @DELETE
    public void deleteSinger(@NotNull @QueryParam("id") Integer id) throws Exception {
        if(!error) {
            logger.info("Delete started for singer by parameter: " + " id " + id + ".");
            ThreadPoolSingleton.getInstance().basicThread(Singer.class, "deleteById", id);
        }
    }

    @POST
    public Singer insertSinger(Singer singer) throws Exception {
        if(!error) {
            logger.info("Add new singer started: " + singer + ".");
            return (Singer) ThreadPoolSingleton.getInstance().basicThread(Singer.class, "add", singer);
        }
        return null;
    }
}

