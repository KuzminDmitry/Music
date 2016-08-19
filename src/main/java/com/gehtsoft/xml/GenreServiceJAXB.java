package com.gehtsoft.xml;

import com.gehtsoft.core.Genre;
import com.gehtsoft.core.GenreList;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acoustics on 03.07.16.
 */
public class GenreServiceJAXB {

    private String inputPath;
    private String outputPath;
    private String xsdPath;

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void setXsdPath(String xsdPath) {
        this.xsdPath = xsdPath;
    }

    public void marshallGenres(List<Genre> genres) {
        if (outputPath == null) {
            return;
        }
        File file = new File(outputPath);
        GenreList genreList = new GenreList();
        genreList.setGenreList(new ArrayList<Genre>());
        for (Genre genre : genres) {
            genreList.getGenreList().add(genre);
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(GenreList.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(genreList, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Genre> unmarshallGenres() {
        if (inputPath == null || xsdPath == null) {
            return new ArrayList<>();
        }
        File file = new File(inputPath);
        List<Genre> genres = new ArrayList<Genre>();
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new File(xsdPath));
            JAXBContext jaxbContext = JAXBContext.newInstance(GenreList.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            GenreList genreList = (GenreList) unmarshaller.unmarshal(file);
            for (Genre genre : genreList.getGenreList()) {
                genres.add(genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genres;
    }
}
