package com.gehtsoft;

import com.gehtsoft.core.Album;
import com.gehtsoft.factory.ServiceFactory;
import com.gehtsoft.iDAO.IBasicService;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dkuzmin on 7/13/2016.
 */
public class AlbumServiceTest {

    @Test
    public void serviceAlbumTest() throws Exception {

        IBasicService albumService = ServiceFactory.getAlbumService();

        List<Album> albums = albumService.getAll();

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

        albumForInsert = (Album)albumService.add(albumForInsert);

        albums = albumService.getAll();
        Integer afterInsertSize = albums.size();

        Assert.assertEquals(Integer.valueOf(afterInsertSize), Integer.valueOf(startSize + 1));

        Album albumById = (Album)albumService.getById(albumForInsert.getId());

        Assert.assertEquals(albumForInsert.getId(), albumById.getId());
        Assert.assertEquals(albumForInsert.getName(), albumById.getName());
        Assert.assertEquals(albumForInsert.getDescription(), albumById.getDescription());
        Assert.assertEquals(albumForInsert.getReleaseDate(), albumById.getReleaseDate());

        Album beforeUpdateAlbum = new Album();
        beforeUpdateAlbum.setId(albumById.getId());
        beforeUpdateAlbum.setName(albumById.getName());
        beforeUpdateAlbum.setDescription(albumById.getDescription());
        beforeUpdateAlbum.setReleaseDate(albumById.getReleaseDate());

        beforeUpdateAlbum.setName("Новое наимнование альбома");

        Album afterUpdateAlbum = (Album)albumService.update(beforeUpdateAlbum);

        Assert.assertEquals(albumById.getId(), beforeUpdateAlbum.getId());
        Assert.assertNotEquals(albumById.getName(), beforeUpdateAlbum.getName());
        Assert.assertEquals(albumById.getDescription(), beforeUpdateAlbum.getDescription());
        Assert.assertEquals(albumById.getReleaseDate(), beforeUpdateAlbum.getReleaseDate());

        albumService.deleteById(afterUpdateAlbum.getId());

        albums = albumService.getAll();

        Integer endSize = albums.size();

        Assert.assertEquals(startSize, endSize);
    }
}
