package com.scdc.csiapp.tablemodel;

import java.io.Serializable;

/**
 * Created by Pantearz07 on 16/9/2559.
 */
public class TbUsers implements Serializable {
    // From Table users field name id_users
    public String id_users = "";

    // From Table users field name id_permission
    public String id_permission = "";

    // From Table users field name pass
    public String pass = "";

    // From Table users field name id_system
    public String id_system = "";

    // From Table users field name title
    public String title = "";

    // From Table users field name name
    public String name = "";

    // From Table users field name surname
    public String surname = "";

    // From Table users field name position
    public String position = "";

    // From Table users field name picture
    public String picture = "";

    // From Table users field name last_login
    public String last_login = "";

    public String getId_users() {
        return id_users;
    }

    public void setId_users(String id_users) {
        this.id_users = id_users;
    }

    public String getId_permission() {
        return id_permission;
    }

    public void setId_permission(String id_permission) {
        this.id_permission = id_permission;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getId_system() {
        return id_system;
    }

    public void setId_system(String id_system) {
        this.id_system = id_system;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }
}
