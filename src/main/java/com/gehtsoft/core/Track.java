package com.gehtsoft.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dkuzmin on 7/6/2016.
 */
@XmlRootElement
public class Track {
    private Integer id;

    private String name;

    private Date releaseDate;

    private List<Integer> singerIds;

    private List<String> singerNames;

    private Integer albumId;
    private String albumName;

    private Integer labelId;

    private String labelName;

    private Integer genreId;

    private String genreName;

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

    public Date getReleaseDate() {
        return releaseDate;
    }

    @XmlElement
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getSingerIds() {
        return singerIds;
    }

    @XmlElement
    public void setSingerIds(List<Integer> singerIds) {
        this.singerIds = singerIds;
    }

    public List<String> getSingerNames() {
        return singerNames;
    }

    public void setSingerNames(List<String> singerNames) {
        this.singerNames = singerNames;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public Track copyTo() {
        Track newTrack = new Track();
        newTrack.setId(this.id);
        newTrack.setName(this.name);
        newTrack.setReleaseDate(this.releaseDate);
        List<Integer> singerIds = new ArrayList<>();
        for(Integer i : this.singerIds){
            singerIds.add(i);
        }
        newTrack.setSingerIds(singerIds);
        List<String> singerNames = new ArrayList<>();
        for(String s : this.singerNames){
            singerNames.add(s);
        }
        newTrack.setSingerNames(singerNames);
        newTrack.setGenreId(this.genreId);
        newTrack.setGenreName(this.genreName);
        newTrack.setAlbumId(this.albumId);
        newTrack.setAlbumName(this.albumName);
        newTrack.setLabelId(this.labelId);
        newTrack.setLabelName(this.labelName);
        return newTrack;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", singerIds=" + singerIds +
                ", singerNames=" + singerNames +
                ", albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", labelId=" + labelId +
                ", labelName='" + labelName + '\'' +
                ", genreId=" + genreId +
                ", genreName='" + genreName + '\'' +
                '}';
    }
}
