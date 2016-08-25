package com.gehtsoft.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkuzmin on 8/24/2016.
 */
@XmlRootElement
public class Role {

    public final static int ADMIN = 1;
    public final static int USER = 2;
    public final static int MODERATOR = 3;

    public static List getRoleIds() {
        List<Integer> roleIds = new ArrayList<>();
        roleIds.add(ADMIN);
        roleIds.add(USER);
        roleIds.add(MODERATOR);
        return roleIds;
    }

    private Integer id;
    private String name;
    private String description;

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

    public Role copyTo(){
        Role newRole = new Role();
        newRole.setId(this.id);
        newRole.setName(this.name);
        newRole.setDescription(this.description);
        return newRole;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
