package com.gehtsoft.xml;

import com.gehtsoft.core.Genre;
import com.gehtsoft.iDAO.IBasicService;

import java.util.*;

/**
 * Created by dkuzmin on 7/13/2016.
 */
public class GenreServiceInMemoryFromXML implements IBasicService {

    private List<Genre> genres = new ArrayList<>();

    private GenreServiceJAXB genreServiceJAXB = new GenreServiceJAXB();

    public GenreServiceInMemoryFromXML(String inputPath, String outputPath, String xsdPath) {
        this.genreServiceJAXB.setOutputPath(outputPath);
        this.genreServiceJAXB.setXsdPath(xsdPath);
        this.genreServiceJAXB.setInputPath(inputPath);
        this.genres = genreServiceJAXB.unmarshallGenres();
        Collections.sort(genres, new Comparator<Genre>() {
            @Override
            public int compare(Genre genre1, Genre genre2) {
                return Integer.compare(genre1.getId(), genre2.getId());
            }
        });
    }

    @Override
    public List<Genre> getAll() throws Exception {
        return this.genres;
    }

    @Override
    public Genre getById(Integer id) throws Exception {
        Genre genre = null;
        Iterator<Genre> iterator = genres.iterator();
        while (iterator.hasNext()) {
            genre = iterator.next();
            if(genre.getId().equals(id)) {
                break;
            }
        }
        return genre;
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        Iterator<Genre> iterator = genres.iterator();
        while (iterator.hasNext()) {
            Genre genre = iterator.next();
            if(genre.getId().equals(id)) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public Genre add(Object object) throws Exception {
        Genre genre = (Genre) object;
        if(genres!=null) {
            genre.setId(genres.get(genres.size() - 1).getId() + 1);
        }else{
            genre.setId(1);
        }
        genres.add(genre);
        return genre;
    }

    @Override
    public Genre update(Object object) throws Exception {
        Genre genre = (Genre) object;
        deleteById(genre.getId());
        add(genre);
        return genre;
    }


}

