package com.gehtsoft;

import com.gehtsoft.core.*;
import com.gehtsoft.factory.ServiceFactory;
import com.gehtsoft.iDAO.*;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by dkuzmin on 7/22/2016.
 */
public class TrackServiceTest extends JerseyTest {

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

    IBasicService singerService = ServiceFactory.getSingerService();

    IBasicService albumService = ServiceFactory.getAlbumService();

    IBasicService genreService = ServiceFactory.getGenreService();

    IBasicService labelService = ServiceFactory.getLabelService();

    ITrackService trackService = ServiceFactory.getTrackService();

    private Genre genre = new Genre();
    private Label label = new Label();
    private Singer singer1 = new Singer();
    private Singer singer2 = new Singer();

    @Before
    public void beforeMethod() throws Exception {
        //Genre part
        List<Genre> genres = genreService.getAll();

        if (genres.size() == 0) {
            String name = "Наименование жанра";
            String description = "Описание жанра";

            genre.setName(name);
            genre.setDescription(description);

            genre = (Genre)genreService.add(genre);
        }

        //Label part

        List<Label> labels = labelService.getAll();

        if (labels.size() == 0) {
            String name = "Наименование лейбла";

            label.setName(name);

            label = (Label)labelService.add(label);
        }

        //Singer part

        List<Singer> singers = singerService.getAll();

        if (singers.size() == 0) {
            String name = "Наименование исполнителя";
            String description = "Описание исполнителя";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String temp = simpleDateFormat.format(new Date());
            Date releaseDate = simpleDateFormat.parse(temp);

            singer1.setName(name);
            singer1.setDescription(description);
            singer1.setBirthDate(releaseDate);

            singer1 = (Singer)singerService.add(singer1);

            singer2 = (Singer)singerService.add(singer2);
        }
    }

    @After
    public void afterMethod() throws Exception {
        if (genre.getId() != null) {
            genreService.deleteById(genre.getId());
        }
        if (label.getId() != null) {
            labelService.deleteById(label.getId());
        }
        if (singer1.getId() != null && singer2.getId() != null) {
            singerService.deleteById(singer1.getId());
            singerService.deleteById(singer2.getId());
        }
    }


    @Test
    public void serviceTrackTest() throws Exception {

        //Album part

        List<Album> albums = albumService.getAll();
        Assert.assertTrue(albums.size() >= 0);

        //Genre part

        List<Genre> genres = genreService.getAll();
        Assert.assertTrue(genres.size() >= 1);

        //Label part

        List<Label> labels = labelService.getAll();
        Assert.assertTrue(labels.size() >= 1);

        //Singer part

        List<Singer> singers = singerService.getAll();
        Assert.assertTrue(singers.size() >= 2);

        //Track part continue
        List<Track> tracks = trackService.getAll();
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

        trackForInsert = (Track)trackService.add(trackForInsert);

        tracks = trackService.getAll();

        Integer afterInsertSize = tracks.size();

        Assert.assertEquals(Integer.valueOf(afterInsertSize), Integer.valueOf(startSize + 1));

        Track trackById = (Track)trackService.getById(trackForInsert.getId());

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

        Track afterUpdateTrack = (Track)trackService.update(beforeUpdateTrack);

        Assert.assertEquals(trackById.getId(), beforeUpdateTrack.getId());
        Assert.assertNotEquals(trackById.getName(), beforeUpdateTrack.getName());
        Assert.assertEquals(trackById.getReleaseDate(), beforeUpdateTrack.getReleaseDate());

        trackService.deleteById(afterUpdateTrack.getId());

        tracks = trackService.getAll();

        Integer endSize = tracks.size();

        Assert.assertEquals(startSize, endSize);
    }
}
