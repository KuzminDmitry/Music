package com.gehtsoft;

import com.gehtsoft.token.Token;
import com.gehtsoft.token.TokenMemorySingleton;
import com.gehtsoft.core.*;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by dkuzmin on 7/22/2016.
 */
public class TrackRestTest extends JerseyTest {

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

    private Genre genre = new Genre();
    private Label label = new Label();
    private Singer singer1 = new Singer();
    private Singer singer2 = new Singer();
    private User user = new User();

    IUserService userService = ServiceFactory.getUserService();

    @Before
    public void beforeMethod() throws Exception {

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

        Cookie cookie = new Cookie("authdata", token.getJwt());

        //Genre part
        Response response = target("genre").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);

        List<Genre> genres = response.readEntity(new GenericType<List<Genre>>() {});

        if (genres.size() == 0) {
            String name = "Наименование жанра";
            String description = "Описание жанра";

            genre.setName(name);
            genre.setDescription(description);

            response = target("genre").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).post(Entity.json(genre), Response.class);

            genre = response.readEntity(Genre.class);
        }

        //Label part
        response = target("label").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);

        List<Label> labels = response.readEntity(new GenericType<List<Label>>() {});

        if (labels.size() == 0) {
            String name = "Наименование лейбла";

            label.setName(name);

            response = target("label").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).post(Entity.json(label), Response.class);

            label = response.readEntity(Label.class);
        }

        //Singer part
        response = target("singer").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);

        List<Singer> singers = response.readEntity(new GenericType<List<Singer>>() {});

        if (singers.size() == 0) {
            String name = "Наименование исполнителя";
            String description = "Описание исполнителя";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String temp = simpleDateFormat.format(new Date());
            Date releaseDate = simpleDateFormat.parse(temp);

            singer1.setName(name);
            singer1.setDescription(description);
            singer1.setBirthDate(releaseDate);

            response = target("singer").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).post(Entity.json(singer1), Response.class);

            singer1 = response.readEntity(Singer.class);

            response = target("singer").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).post(Entity.json(singer1), Response.class);

            singer2 = response.readEntity(Singer.class);
        }
    }

    @After
    public void afterMethod() throws Exception {
        Cookie cookie = new Cookie("authdata", token.getJwt());
        if (genre.getId() != null) {
            target("genre").queryParam("id", genre.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).delete(Response.class);
        }
        if (label.getId() != null) {
            target("label").queryParam("id", label.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).delete(Response.class);
        }
        if (singer1.getId() != null && singer2.getId() != null) {
            target("singer").queryParam("id", singer1.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).delete(Response.class);
            target("singer").queryParam("id", singer2.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).delete(Response.class);
        }
        if(user.getId()!=null){
            userService.deleteById(user.getId());
        }
        TokenMemorySingleton.getInstance().deleteToken(token);
    }


    @Test
    public void restTrackTest() throws Exception {

        Cookie fakeCookie = new Cookie("authdata", "fakeCookie");

        Response response = target("track").request().cookie(fakeCookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(403, response.getStatus());

        Cookie cookie = new Cookie("authdata", token.getJwt());

        //Album part
        response = target("album").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());

        List<Album> albums = response.readEntity(new GenericType<List<Album>>() {});
        Assert.assertTrue(albums.size() >= 0);

        //Genre part
        response = target("genre").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());

        List<Genre> genres = response.readEntity(new GenericType<List<Genre>>() {});
        Assert.assertTrue(genres.size() >= 1);

        //Label part
        response = target("label").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());

        List<Label> labels = response.readEntity(new GenericType<List<Label>>() {});
        Assert.assertTrue(labels.size() >= 1);

        //Singer part
        response = target("singer").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());

        List<Singer> singers = response.readEntity(new GenericType<List<Singer>>() {});
        Assert.assertTrue(singers.size() >= 2);

        //Track part continue

        response = target("track").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());

        List<Track> tracks = response.readEntity(new GenericType<List<Track>>() {});
        Assert.assertTrue(tracks.size() >= 0);

        Integer startSize = tracks.size();


//        private Integer id;
//        private String name;
//        private Date releaseDate;
//        private List<Integer> singerIds;
//        private List<String> singerNames;
//        private Integer albumId;
//        private String albumName;
//        private Integer labelId;
//        private String labelName;
//        private Integer genreId;
//        private String genreName;

        String name = "Наименование трека";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String temp = simpleDateFormat.format(new Date());
        Date releaseDate = simpleDateFormat.parse(temp);


        List<Integer> singerIds = new ArrayList<>();
        List<String> singerNames = new ArrayList<>();

        if (singers.size() >= 2) {
            for (int i = 0; i < 2; i++) {
                singerIds.add(singers.get(i).getId());
                singerNames.add(singers.get(i).getName());
            }
        }

        Collections.sort(singerIds);
        Collections.sort(singerNames);

        Integer albumId = null;
        String albumName = null;

        if (albums.size() != 0) {
            albumId = albums.get(0).getId();
            albumName = albums.get(0).getName();

        }

        Integer labelId = null;
        String labelName = null;

        if (labels.size() != 0) {
            labelId = labels.get(0).getId();
            labelName = labels.get(0).getName();
        }

        Integer genreId = null;
        String genreName = null;

        if (genres.size() != 0) {
            genreId = genres.get(0).getId();
            genreName = genres.get(0).getName();
        }


        Track trackForInsert = new Track();
        trackForInsert.setName(name);
        trackForInsert.setReleaseDate(releaseDate);
        trackForInsert.setSingerIds(singerIds);
        trackForInsert.setSingerNames(singerNames);
        trackForInsert.setGenreId(genreId);
        trackForInsert.setGenreName(genreName);
        trackForInsert.setLabelId(labelId);
        trackForInsert.setLabelName(labelName);
        trackForInsert.setAlbumId(albumId);
        trackForInsert.setAlbumName(albumName);

        response = target("track").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).post(Entity.json(trackForInsert), Response.class);
        Assert.assertEquals(200, response.getStatus());

        trackForInsert = response.readEntity(Track.class);

        response = target("track").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        tracks = response.readEntity(new GenericType<List<Track>>() {});

        Integer afterInsertSize = tracks.size();

        Assert.assertEquals(Integer.valueOf(afterInsertSize), Integer.valueOf(startSize + 1));

        response = target("track").path(trackForInsert.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Track trackById = response.readEntity(Track.class);
        Assert.assertEquals(200, response.getStatus());


        Collections.sort(trackById.getSingerIds());
        Collections.sort(trackById.getSingerNames());

        Assert.assertEquals(trackForInsert.getId(), trackById.getId());
        Assert.assertEquals(trackForInsert.getName(), trackById.getName());
        Assert.assertEquals(trackForInsert.getReleaseDate(), trackById.getReleaseDate());
        Assert.assertEquals(trackForInsert.getSingerIds().size(), trackById.getSingerIds().size());


        Assert.assertEquals(trackForInsert.getSingerIds().size(), trackById.getSingerIds().size());

        if (trackForInsert.getSingerIds().size() == trackById.getSingerIds().size()) {
            for (int i = 0; i < trackForInsert.getSingerIds().size(); i++) {
                Assert.assertEquals(trackForInsert.getSingerIds().get(i), trackById.getSingerIds().get(i));
            }
        }

        Assert.assertEquals(trackForInsert.getSingerNames().size(), trackById.getSingerNames().size());

        if (trackForInsert.getSingerNames().size() == trackById.getSingerNames().size()) {
            for (int i = 0; i < trackForInsert.getSingerNames().size(); i++) {
                Assert.assertEquals(trackForInsert.getSingerNames().get(i), trackById.getSingerNames().get(i));
            }
        }

        Assert.assertEquals(trackForInsert.getGenreId(), trackById.getGenreId());
        Assert.assertEquals(trackForInsert.getGenreName(), trackById.getGenreName());
        Assert.assertEquals(trackForInsert.getLabelId(), trackById.getLabelId());
        Assert.assertEquals(trackForInsert.getLabelName(), trackById.getLabelName());
        Assert.assertEquals(trackForInsert.getAlbumId(), trackById.getAlbumId());
        Assert.assertEquals(trackForInsert.getAlbumName(), trackById.getAlbumName());

        Track beforeUpdateTrack = trackById.copyTo();

        beforeUpdateTrack.setName("Новое наименование трека");

        response = target("track").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).put(Entity.json(beforeUpdateTrack), Response.class);
        Assert.assertEquals(200, response.getStatus());
        Track afterUpdateTrack = response.readEntity(Track.class);

        Assert.assertEquals(trackById.getId(), beforeUpdateTrack.getId());
        Assert.assertNotEquals(trackById.getName(), beforeUpdateTrack.getName());
        Assert.assertEquals(trackById.getReleaseDate(), beforeUpdateTrack.getReleaseDate());

        response = target("track").queryParam("id", afterUpdateTrack.getId().toString()).request().cookie(cookie).accept(MediaType.APPLICATION_JSON).delete(Response.class);
        Assert.assertEquals(204, response.getStatus());

        response = target("track").request().cookie(cookie).accept(MediaType.APPLICATION_JSON).get(Response.class);
        Assert.assertEquals(200, response.getStatus());
        tracks = response.readEntity(new GenericType<List<Track>>() {});

        Integer endSize = tracks.size();

        Assert.assertEquals(startSize, endSize);
    }
}
