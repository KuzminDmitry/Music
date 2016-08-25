package com.gehtsoft;

import com.gehtsoft.core.Role;
import com.gehtsoft.token.Token;
import com.gehtsoft.token.TokenMemorySingleton;
import com.gehtsoft.core.Album;
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
public class AlbumRestTest extends JerseyTest {

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
    public void restAlbumTest() throws Exception {

        Cookie fakeCookie = new Cookie("authdata", "fakeCookie");

        Response response = target("album").request().cookie(fakeCookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(403, response.getStatus());

        Cookie cookie = new Cookie("authdata", token.getJwt());

        response = target("album").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());

        List<Album> albums = response.readEntity(new GenericType<List<Album>>() {});

        Integer startSize = albums.size();

        String name = "Наименование альбома";
        String description = "Описание альбома";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String temp = simpleDateFormat.format(new Date());
        Date releaseDate = simpleDateFormat.parse(temp);

        Album albumForInsert = new Album();
        albumForInsert.setName(name);
        albumForInsert.setDescription(description);
        albumForInsert.setReleaseDate(releaseDate);

        response = target("album").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).post(Entity.json(albumForInsert), Response.class);
        Assert.assertEquals(200, response.getStatus());

        albumForInsert = response.readEntity(Album.class);

        response = target("album").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        albums = response.readEntity(new GenericType<List<Album>>() {});

        Integer afterInsertSize = albums.size();

        Assert.assertEquals(Integer.valueOf(afterInsertSize), Integer.valueOf(startSize + 1));

        response = target("album").path(albumForInsert.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        Album albumById = response.readEntity(Album.class);

        Assert.assertEquals(albumForInsert.getId(), albumById.getId());
        Assert.assertEquals(albumForInsert.getName(), albumById.getName());
        Assert.assertEquals(albumForInsert.getDescription(), albumById.getDescription());
        Assert.assertEquals(albumForInsert.getReleaseDate(), albumById.getReleaseDate());

        Album beforeUpdateAlbum = new Album();
        beforeUpdateAlbum.setId(albumById.getId());
        beforeUpdateAlbum.setName(albumById.getName());
        beforeUpdateAlbum.setDescription(albumById.getDescription());
        beforeUpdateAlbum.setReleaseDate(albumById.getReleaseDate());

        beforeUpdateAlbum.setName("Новое наименование альбома");

        response = target("album").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).put(Entity.json(beforeUpdateAlbum), Response.class);
        Assert.assertEquals(200, response.getStatus());
        Album afterUpdateAlbum = response.readEntity(Album.class);

        Assert.assertEquals(albumById.getId(), beforeUpdateAlbum.getId());
        Assert.assertNotEquals(albumById.getName(), beforeUpdateAlbum.getName());
        Assert.assertEquals(albumById.getDescription(), beforeUpdateAlbum.getDescription());
        Assert.assertEquals(albumById.getReleaseDate(), beforeUpdateAlbum.getReleaseDate());

        response = target("album").queryParam("id", afterUpdateAlbum.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).delete(Response.class);
        Assert.assertEquals(204, response.getStatus());

        response = target("album").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        albums = response.readEntity(new GenericType<List<Album>>() {});

        Integer endSize = albums.size();

        Assert.assertEquals(startSize, endSize);

    }

}
