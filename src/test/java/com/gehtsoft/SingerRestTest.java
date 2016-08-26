package com.gehtsoft;

import com.gehtsoft.core.Role;
import com.gehtsoft.token.Token;
import com.gehtsoft.token.TokenMemorySingleton;
import com.gehtsoft.core.Singer;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dkuzmin on 7/22/2016.
 */
public class SingerRestTest extends JerseyTest {

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
        roleIds.add(Role.USER);
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
    public void restSingerTest() throws Exception {

        Cookie fakeCookie = new Cookie("authdata", "fakeCookie");

        Response response = target("singer").request().cookie(fakeCookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(401, response.getStatus());

        Cookie cookie = new Cookie("authdata", token.getJwt());

        response = target("singer").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());

        List<Singer> singers = response.readEntity(new GenericType<List<Singer>>() {});

        Integer startSize = singers.size();

        String name = "Наименование исполнителя";
        String description = "Описание исполнителя";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String temp = simpleDateFormat.format(new Date());
        Date releaseDate = simpleDateFormat.parse(temp);

        Singer singerForInsert = new Singer();
        singerForInsert.setName(name);
        singerForInsert.setDescription(description);
        singerForInsert.setBirthDate(releaseDate);

        response = target("singer").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).post(Entity.json(singerForInsert), Response.class);
        Assert.assertEquals(200, response.getStatus());

        singerForInsert = response.readEntity(Singer.class);

        response = target("singer").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        singers = response.readEntity(new GenericType<List<Singer>>() {});

        Integer afterInsertSize = singers.size();

        Assert.assertEquals(Integer.valueOf(afterInsertSize), Integer.valueOf(startSize + 1));

        response = target("singer").path(singerForInsert.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        Singer singerById = response.readEntity(Singer.class);

        Assert.assertEquals(singerForInsert.getId(), singerById.getId());
        Assert.assertEquals(singerForInsert.getName(), singerById.getName());
        Assert.assertEquals(singerForInsert.getDescription(), singerById.getDescription());
        Assert.assertEquals(singerForInsert.getBirthDate(), singerById.getBirthDate());

        Singer beforeUpdateSinger = new Singer();
        beforeUpdateSinger.setId(singerById.getId());
        beforeUpdateSinger.setName(singerById.getName());
        beforeUpdateSinger.setDescription(singerById.getDescription());
        beforeUpdateSinger.setBirthDate(singerById.getBirthDate());

        beforeUpdateSinger.setName("Новое наименование исполнителя");

        response = target("singer").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).put(Entity.json(beforeUpdateSinger), Response.class);
        Assert.assertEquals(200, response.getStatus());
        Singer afterUpdateSinger = response.readEntity(Singer.class);

        Assert.assertEquals(singerById.getId(), beforeUpdateSinger.getId());
        Assert.assertNotEquals(singerById.getName(), beforeUpdateSinger.getName());
        Assert.assertEquals(singerById.getDescription(), beforeUpdateSinger.getDescription());
        Assert.assertEquals(singerById.getBirthDate(), beforeUpdateSinger.getBirthDate());

        response = target("singer").queryParam("id", afterUpdateSinger.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).delete(Response.class);
        Assert.assertEquals(204, response.getStatus());

        response = target("singer").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        singers = response.readEntity(new GenericType<List<Singer>>() {});

        Integer endSize = singers.size();

        Assert.assertEquals(startSize, endSize);

    }

}
