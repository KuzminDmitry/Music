package com.gehtsoft;

import com.gehtsoft.core.Genre;
import com.gehtsoft.factory.ServiceFactory;
import com.gehtsoft.iDAO.IBasicService;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by dkuzmin on 7/13/2016.
 */
public class GenreServiceTest {

    @Test
    public void serviceGenreTest() throws Exception {

        IBasicService genreService = ServiceFactory.getGenreService();

        List<Genre> genres = genreService.getAll();

        Integer startSize = genres.size();

        String name = "Название жанра";
        String description = "Описание жанра";

        Genre genreForInsert = new Genre();
        genreForInsert.setName(name);
        genreForInsert.setDescription(description);

        genreForInsert = (Genre)genreService.add(genreForInsert);

        genres = genreService.getAll();
        Integer afterInsertSize = genres.size();

        Assert.assertEquals(Integer.valueOf(afterInsertSize), Integer.valueOf(startSize + 1));

        Genre genreById = (Genre)genreService.getById(genreForInsert.getId());

        Assert.assertEquals(genreForInsert.getId(), genreById.getId());
        Assert.assertEquals(genreForInsert.getName(), genreById.getName());
        Assert.assertEquals(genreForInsert.getDescription(), genreById.getDescription());

        Genre beforeUpdateGenre = new Genre();
        beforeUpdateGenre.setId(genreById.getId());
        beforeUpdateGenre.setName(genreById.getName());
        beforeUpdateGenre.setDescription(genreById.getDescription());

        beforeUpdateGenre.setName("Новое название жанра");

        Genre afterUpdateGenre = (Genre)genreService.update(beforeUpdateGenre);

        Assert.assertEquals(genreById.getId(), beforeUpdateGenre.getId());
        Assert.assertNotEquals(genreById.getName(), beforeUpdateGenre.getName());
        Assert.assertEquals(genreById.getDescription(), beforeUpdateGenre.getDescription());

        genreService.deleteById(afterUpdateGenre.getId());

        genres = genreService.getAll();

        Integer endSize = genres.size();

        Assert.assertEquals(startSize, endSize);
    }
}
