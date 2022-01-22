package com.kozbook.server.entity.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "user")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id_library")
public class Library implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id_library;
    protected String name;

    protected List<User> users;

    public Library() {
    }

    public Library(int id_library, String name) {
        this.id_library = id_library;
        this.name = name;
    }

    public int getId_library() {
        return id_library;
    }

    public void setId_library(int id_library) {
        this.id_library = id_library;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
