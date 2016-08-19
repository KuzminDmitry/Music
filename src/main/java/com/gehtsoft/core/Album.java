package com.gehtsoft.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
/**
 * Created by dkuzmin on 7/6/2016.
 */
@XmlRootElement
public class Album {
    private Integer id;

    private String name;

    private String description;

    private Date releaseDate;

    public Integer getId() {
        return id;
    }

    @XmlAttribute
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    @XmlElement
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Album copyTo(){
        Album newAlbum = new Album();
        newAlbum.setId(this.id);
        newAlbum.setName(this.name);
        newAlbum.setDescription(this.description);
        newAlbum.setReleaseDate(this.releaseDate);
        return newAlbum;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                '}';
    }
}
