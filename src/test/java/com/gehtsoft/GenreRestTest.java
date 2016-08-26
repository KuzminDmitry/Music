package com.gehtsoft;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gehtsoft.core.Role;
import com.gehtsoft.token.Token;
import com.gehtsoft.token.TokenMemorySingleton;
import com.gehtsoft.core.Genre;
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

/**
 * Created by dkuzmin on 7/22/2016.
 */
public class GenreRestTest extends JerseyTest {

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
    public void restGenreTest() throws Exception {

        Cookie fakeCookie = new Cookie("authdata", "fakeCookie");

        Response response = target("genre").request().cookie(fakeCookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(401, response.getStatus());

        Cookie cookie = new Cookie("authdata", token.getJwt());

        response = target("genre").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());

        List<Genre> genres = response.readEntity(new GenericType<List<Genre>>() {});

        Integer startSize = genres.size();

        String name = "Наименование жанра";
        String description = "Описание жанра";

        Genre genreForInsert = new Genre();
        genreForInsert.setName(name);
        genreForInsert.setDescription(description);

        response = target("genre").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).post(Entity.json(genreForInsert), Response.class);
        Assert.assertEquals(200, response.getStatus());

        genreForInsert = response.readEntity(Genre.class);

        response = target("genre").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        genres = response.readEntity(new GenericType<List<Genre>>() {});

        Integer afterInsertSize = genres.size();

        Assert.assertEquals(Integer.valueOf(afterInsertSize), Integer.valueOf(startSize + 1));

        response = target("genre").path(genreForInsert.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        Genre genreById = response.readEntity(Genre.class);

        Assert.assertEquals(genreForInsert.getId(), genreById.getId());
        Assert.assertEquals(genreForInsert.getName(), genreById.getName());
        Assert.assertEquals(genreForInsert.getDescription(), genreById.getDescription());

        Genre beforeUpdateGenre = new Genre();
        beforeUpdateGenre.setId(genreById.getId());
        beforeUpdateGenre.setName(genreById.getName());
        beforeUpdateGenre.setDescription(genreById.getDescription());

        beforeUpdateGenre.setName("Новое наименование жанра");

        response = target("genre").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).put(Entity.json(beforeUpdateGenre), Response.class);
        Assert.assertEquals(200, response.getStatus());
        Genre afterUpdateGenre = response.readEntity(Genre.class);

        Assert.assertEquals(genreById.getId(), beforeUpdateGenre.getId());
        Assert.assertNotEquals(genreById.getName(), beforeUpdateGenre.getName());
        Assert.assertEquals(genreById.getDescription(), beforeUpdateGenre.getDescription());

        response = target("genre").queryParam("id", afterUpdateGenre.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).delete(Response.class);
        Assert.assertEquals(204, response.getStatus());

        response = target("genre").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        genres = response.readEntity(new GenericType<List<Genre>>() {});

        Integer endSize = genres.size();

        Assert.assertEquals(startSize, endSize);

    }

}
