package com.gehtsoft.rest;

import com.gehtsoft.auth.IAuthChecker;
import com.gehtsoft.core.Singer;
import com.gehtsoft.factory.SecurityFactory;
import com.gehtsoft.threadPool.ThreadPoolSingleton;
import com.gehtsoft.token.Token;
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
@Path("singer")
@Produces("application/json; charset=utf-8")
public class SingerResource {

    final static Logger logger = Logger.getLogger("resource");

    private Token token = null;

    @Context
    ServletContext servletContext;

    @Context
    HttpServletResponse response;

    @Context
    HttpServletRequest request;

    @Context
    public void checkAuthenticate(HttpServletRequest httpServletRequest) throws Exception {
        IAuthChecker authChecker = SecurityFactory.getAuthChecker();
        if ((token = authChecker.validate(this.request)) == null) {
            response.sendError(401);
        }
    }

    @GET
    public List<Singer> getAllSingers() throws Exception {
        if(token == null) return null;
        logger.info("Get all singers started.");
        return (List) ThreadPoolSingleton.getInstance().basicThread(Singer.class, "getAll", null);

    }

    @GET
    @Path("{id}")
    public Singer getSingerById(@NotNull @PathParam("id") Integer id) throws Exception {
        if(token == null) return null;
        logger.info("Search started for singer by parameter: " + " id " + id + ".");
        return (Singer) ThreadPoolSingleton.getInstance().basicThread(Singer.class, "getById", id);

    }

    @PUT
    public Singer updateSinger(Singer singer) throws Exception {
        if(token == null) return null;
        logger.info("Update started for singer by singer track: " + singer + ".");
        return (Singer) ThreadPoolSingleton.getInstance().basicThread(Singer.class, "update", singer);

    }

    @DELETE
    public void deleteSinger(@NotNull @QueryParam("id") Integer id) throws Exception {
        if(token == null) return;
        logger.info("Delete started for singer by parameter: " + " id " + id + ".");
        ThreadPoolSingleton.getInstance().basicThread(Singer.class, "deleteById", id);

    }

    @POST
    public Singer insertSinger(Singer singer) throws Exception {
        if(token == null) return null;
        logger.info("Add new singer started: " + singer + ".");
        return (Singer) ThreadPoolSingleton.getInstance().basicThread(Singer.class, "add", singer);

    }
}

