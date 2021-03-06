package com.gehtsoft.rest;

import com.gehtsoft.auth.IAuthChecker;
import com.gehtsoft.core.Track;
import com.gehtsoft.factory.SecurityFactory;
import com.gehtsoft.threadPool.ThreadPoolSingleton;
import com.gehtsoft.token.Token;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkuzmin on 7/6/2016.
 */
@Path("track")
@Produces("application/json; charset=utf-8")
public class TrackResource {

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
    public List<Track> getAllTracks() throws Exception {
        if(token == null) return null;
        logger.info("Get all tracks started.");
        return (List) ThreadPoolSingleton.getInstance().trackThread(Track.class, "getAll", null);

    }

    @GET
    @Path("find")
    public List<Track> getAllTracksByFilter(@QueryParam("singerId") Integer singerId, @QueryParam("albumId") Integer albumId, @QueryParam("labelId") Integer labelId, @QueryParam("genreId") Integer genreId) throws Exception {
        if(token == null) return null;
        Track track = new Track();
        track.setAlbumId(albumId);
        track.setGenreId(genreId);
        track.setLabelId(labelId);
        List<Integer> singerIds = new ArrayList<>();
        singerIds.add(singerId);
        track.setSingerIds(singerIds);
        logger.info("Search started for tracks by parameters: " + "singerId=" + singerId + "; " + "albumId=" + albumId + "; " + "labelId=" + labelId + "; " + "genreId=" + genreId + ".");
        return (List) ThreadPoolSingleton.getInstance().trackThread(Track.class, "getAllByFilter", track);

    }

    @GET
    @Path("{id}")
    public Track getTrackById(@NotNull @PathParam("id") Integer id) throws Exception {
        if(token == null) return null;
        logger.info("Search started for track by parameter: " + " id " + id + ".");
        return (Track) ThreadPoolSingleton.getInstance().trackThread(Track.class, "getById", id);

    }

    @DELETE
    public void deleteTrack(@NotNull @QueryParam("id") Integer id) throws Exception {
        if(token == null) return;
        logger.info("Delete started for track by parameter: " + " id " + id + ".");
        ThreadPoolSingleton.getInstance().trackThread(Track.class, "deleteById", id);

    }

    @PUT
    public Track updateTrack(Track track) throws Exception {
        if(token == null) return null;
        logger.info("Update started for track by new track: " + track + ".");
        return (Track) ThreadPoolSingleton.getInstance().trackThread(Track.class, "update", track);

    }

    @POST
    public Track insertTrack(Track track) throws Exception {
        if(token == null) return null;
        logger.info("Add new track started: " + track + ".");
        return (Track) ThreadPoolSingleton.getInstance().trackThread(Track.class, "add", track);

    }
}

