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
import java.util.List;

/**
 * Created by dkuzmin on 7/6/2016.
 */
@Path("label")
@Produces("application/json; charset=utf-8")
public class LabelResource {

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
    public List<Label> getAllLabels() throws Exception {
        if(!error) {
            logger.info("Get all labels started.");
            return (List) ThreadPoolSingleton.getInstance().basicThread(Label.class, "getAll", null);
        }
        return null;
    }

    @GET
    @Path("{id}")
    public Label getLabelById(@NotNull @PathParam("id") Integer id) throws Exception {
        if(!error) {
            logger.info("Search started for label by parameter: " + " id " + id + ".");
            return (Label) ThreadPoolSingleton.getInstance().basicThread(Label.class, "getById", id);
        }
        return null;
    }

    @PUT
    public Label updateLabel(Label label) throws Exception {
        if(!error) {
            logger.info("Update started for label by singer track: " + label + ".");
            return (Label) ThreadPoolSingleton.getInstance().basicThread(Label.class, "update", label);
        }
        return null;
    }

    @DELETE
    public void deleteLabel(@NotNull @QueryParam("id") Integer id) throws Exception {
        if(!error) {
            logger.info("Delete started for label by parameter: " + " id " + id + ".");
            ThreadPoolSingleton.getInstance().basicThread(Label.class, "deleteById", id);
        }
    }

    @POST
    public Label insertLabel(Label label) throws Exception {
        if(!error) {
            logger.info("Add new label started: " + label + ".");
            return (Label) ThreadPoolSingleton.getInstance().basicThread(Label.class, "add", label);
        }
        return null;
    }
}

