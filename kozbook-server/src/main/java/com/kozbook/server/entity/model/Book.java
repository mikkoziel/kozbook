package com.kozbook.server.entity.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "book")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id_book")
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id_book;

    protected String name;
    protected int isbn;
//    protected  surname;
}
