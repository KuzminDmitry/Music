package com.gehtsoft.rest;

import com.gehtsoft.auth.AuthenticateChecker;
import com.gehtsoft.token.Token;
import com.gehtsoft.token.TokenMemorySingleton;
import com.gehtsoft.core.Label;
import com.gehtsoft.threadPool.ThreadPoolSingleton;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by dkuzmin on 7/6/2016.
 */
@Path("label")
@Produces("application/json; charset=utf-8")
public class LabelResource {

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
    public List<Label> getAllLabels() throws Exception {
        if (!isAuthorized) return null;
        logger.info("Get all labels started.");
        return (List) ThreadPoolSingleton.getInstance().basicThread(Label.class, "getAll", null);

    }

    @GET
    @Path("{id}")
    public Label getLabelById(@NotNull @PathParam("id") Integer id) throws Exception {
        if (!isAuthorized) return null;
        logger.info("Search started for label by parameter: " + " id " + id + ".");
        return (Label) ThreadPoolSingleton.getInstance().basicThread(Label.class, "getById", id);
    }

    @PUT
    public Label updateLabel(Label label) throws Exception {
        if (!isAuthorized) return null;
        logger.info("Update started for label by singer track: " + label + ".");
        return (Label) ThreadPoolSingleton.getInstance().basicThread(Label.class, "update", label);
    }

    @DELETE
    public void deleteLabel(@NotNull @QueryParam("id") Integer id) throws Exception {
        if (!isAuthorized) return;
        logger.info("Delete started for label by parameter: " + " id " + id + ".");
        ThreadPoolSingleton.getInstance().basicThread(Label.class, "deleteById", id);
    }

    @POST
    public Label insertLabel(Label label) throws Exception {
        if (!isAuthorized) return null;
        logger.info("Add new label started: " + label + ".");
        return (Label) ThreadPoolSingleton.getInstance().basicThread(Label.class, "add", label);
    }
}

