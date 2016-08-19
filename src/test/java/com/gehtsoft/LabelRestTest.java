package com.gehtsoft;

import com.gehtsoft.token.Token;
import com.gehtsoft.token.TokenMemorySingleton;
import com.gehtsoft.core.Label;
import com.gehtsoft.core.User;
import com.gehtsoft.factory.ServiceFactory;
import com.gehtsoft.iDAO.IUserService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkuzmin on 7/22/2016.
 */
public class LabelRestTest extends JerseyTest {

    @Override
    protected DeploymentContext configureDeployment() {
        return ServletDeploymentContext.builder(new ResourceConfig())
                .initParam(ServerProperties.PROVIDER_PACKAGES, this.getClass().getPackage().getName())
                .build();
    }

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    private Token token;

    private User user = new User();

    IUserService userService = ServiceFactory.getUserService();

    @Before
    public void beforeMethod() throws Exception{
        user.setUserName("newuser");
        user.setPassword("hispassword");
        List<Integer> roleIds = new ArrayList<>();
        roleIds.add(1);
        user.setRoleIds(roleIds);
        List<String> roleNames = new ArrayList<>();
        roleNames.add("User");
        user.setRoleNames(roleNames);

        user = (User)userService.add(user);

        token = TokenMemorySingleton.getInstance().addToken(user);
    }

    @After
    public void afterMethod() throws Exception {
        if(user.getId()!=null){
            userService.deleteById(user.getId());
        }
        TokenMemorySingleton.getInstance().deleteToken(token);
    }

    @Test
    public void restLabelTest() throws Exception {

        Cookie fakeCookie = new Cookie("authdata", "fakeCookie");

        Response response = target("label").request().cookie(fakeCookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(403, response.getStatus());

        Cookie cookie = new Cookie("authdata", token.getJwt());

        response = target("label").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());

        List<Label> labels = response.readEntity(new GenericType<List<Label>>() {});

        Integer startSize = labels.size();

        String name = "Наименование лейбла";

        Label labelForInsert = new Label();
        labelForInsert.setName(name);

        response = target("label").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).post(Entity.json(labelForInsert), Response.class);
        Assert.assertEquals(200, response.getStatus());

        labelForInsert = response.readEntity(Label.class);

        response = target("label").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        labels = response.readEntity(new GenericType<List<Label>>() {});

        Integer afterInsertSize = labels.size();

        Assert.assertEquals(Integer.valueOf(afterInsertSize), Integer.valueOf(startSize + 1));

        response = target("label").path(labelForInsert.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        Label labelById = response.readEntity(Label.class);

        Assert.assertEquals(labelForInsert.getId(), labelById.getId());
        Assert.assertEquals(labelForInsert.getName(), labelById.getName());

        Label beforeUpdateLabel = new Label();
        beforeUpdateLabel.setId(labelById.getId());
        beforeUpdateLabel.setName(labelById.getName());

        beforeUpdateLabel.setName("Новое наименование лейбла");

        response = target("label").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).put(Entity.json(beforeUpdateLabel), Response.class);
        Assert.assertEquals(200, response.getStatus());
        Label afterUpdateLabel = response.readEntity(Label.class);

        Assert.assertEquals(labelById.getId(), beforeUpdateLabel.getId());
        Assert.assertNotEquals(labelById.getName(), beforeUpdateLabel.getName());

        response = target("label").queryParam("id", afterUpdateLabel.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).delete(Response.class);
        Assert.assertEquals(204, response.getStatus());

        response = target("label").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        labels = response.readEntity(new GenericType<List<Label>>() {});

        Integer endSize = labels.size();

        Assert.assertEquals(startSize, endSize);

    }

}
