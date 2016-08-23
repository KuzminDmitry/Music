package com.gehtsoft.rest;

import com.gehtsoft.auth.AuthenticateChecker;
import com.gehtsoft.token.Token;
import com.gehtsoft.token.TokenMemorySingleton;
import com.gehtsoft.core.Album;
import com.gehtsoft.threadPool.ThreadPoolSingleton;
import org.apache.http.auth.AuthenticationException;
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
@Path("album")
@Produces("application/json; charset=utf-8")
public class AlbumResource {

    final static Logger logger = Logger.getLogger("resource");

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
    public List<Album> getAllAlbums() throws Exception {
        logger.info("Get all albums started.");
        return (List) ThreadPoolSingleton.getInstance().basicThread(Album.class, "getAll", null);
    }

    @GET
    @Path("{id}")
    public Album getAlbumById(@NotNull @PathParam("id") Integer id) throws Exception {
        logger.info("Search started for album by parameter: " + " id " + id + ".");
        return (Album) ThreadPoolSingleton.getInstance().basicThread(Album.class, "getById", id);
    }

    @PUT
    public Album updateAlbum(Album album) throws Exception {
        logger.info("Update started for album by singer track: " + album + ".");
        return (Album) ThreadPoolSingleton.getInstance().basicThread(Album.class, "update", album);
    }

    @DELETE
    public void deleteAlbum(@NotNull @QueryParam("id") Integer id) throws Exception {
        logger.info("Delete started for album by parameter: " + " id " + id + ".");
        ThreadPoolSingleton.getInstance().basicThread(Album.class, "deleteById", id);
    }

    @POST
    public Album insertAlbum(Album album) throws Exception {
        logger.info("Add new album started: " + album + ".");
        return (Album) ThreadPoolSingleton.getInstance().basicThread(Album.class, "add", album);
    }
}

