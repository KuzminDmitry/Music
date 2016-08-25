package com.gehtsoft.rest;

import com.gehtsoft.auth.AuthenticateChecker;
import com.gehtsoft.core.Album;
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
@Path("album")
@Produces("application/json; charset=utf-8")
public class AlbumResource {

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
        if ((token = AuthenticateChecker.validate(this.request)) == null) {
            response.sendError(403);
        }
    }

    @GET
    public List<Album> getAllAlbums() throws Exception {
        if(token == null) return null;
        logger.info("Get all albums started.");
        return (List) ThreadPoolSingleton.getInstance().basicThread(Album.class, "getAll", null);
    }

    @GET
    @Path("{id}")
    public Album getAlbumById(@NotNull @PathParam("id") Integer id) throws Exception {
        if(token == null) return null;
        logger.info("Search started for album by parameter: " + " id " + id + ".");
        return (Album) ThreadPoolSingleton.getInstance().basicThread(Album.class, "getById", id);
    }

    @PUT
    public Album updateAlbum(Album album) throws Exception {
        if(token == null) return null;
        logger.info("Update started for album by singer track: " + album + ".");
        return (Album) ThreadPoolSingleton.getInstance().basicThread(Album.class, "update", album);
    }

    @DELETE
    public void deleteAlbum(@NotNull @QueryParam("id") Integer id) throws Exception {
        if(token == null) return;
        logger.info("Delete started for album by parameter: " + " id " + id + ".");
        ThreadPoolSingleton.getInstance().basicThread(Album.class, "deleteById", id);
    }

    @POST
    public Album insertAlbum(Album album) throws Exception {
        if(token == null) return null;
        logger.info("Add new album started: " + album + ".");
        return (Album) ThreadPoolSingleton.getInstance().basicThread(Album.class, "add", album);
    }
}

