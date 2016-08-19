package com.gehtsoft.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkuzmin on 7/6/2016.
 */
@XmlRootElement
public class User {
    private Integer id;

    private String userName;

    private String password;

    private List<Integer> roleIds;

    private List<String> roleNames;

    public Integer getId() {
        return id;
    }

    @XmlAttribute
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    @XmlElement
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    @XmlElement
    public void setPassword(String password) {
        this.password = password;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    @XmlElement
    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    @XmlElement
    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public User copyTo() {
        User newUser = new User();
        newUser.setId(this.id);
        newUser.setUserName(this.userName);
        newUser.setPassword(this.password);
        List<Integer> roleIds = new ArrayList<>();
        for(Integer i : this.roleIds){
            roleIds.add(i);
        }
        newUser.setRoleIds(roleIds);
        List<String> roleNames = new ArrayList<>();
        for(String s : this.roleNames){
            roleNames.add(s);
        }
        newUser.setRoleNames(roleNames);
        return newUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", roleIds=" + roleIds +
                ", roleNames=" + roleNames +
                '}';
    }
}
