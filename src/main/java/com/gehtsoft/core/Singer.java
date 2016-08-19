package com.gehtsoft.core;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by dkuzmin on 7/6/2016.
 */
@XmlRootElement
public class Singer {
    private Integer id;

    private String name;

    private String description;

    private Date birthDate;

    public Singer() {
    }

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

    public Date getBirthDate() {
        return birthDate;
    }

    @XmlElement
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Singer copyTo(){
        Singer newSinger = new Singer();
        newSinger.setId(this.id);
        newSinger.setName(this.name);
        newSinger.setDescription(this.description);
        newSinger.setBirthDate(this.birthDate);
        return newSinger;
    }

    @Override
    public String toString() {
        return "Singer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
