package com.example.Polka.database;

import jakarta.persistence.*;

@Entity
@Table(name = "bookspolka")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "genre")
    private int genre;
    @Column(name = "author")
    private String author;
    @Column(name = "title")
    private String title;
    @Column(name = "about")
    private String about;
    @Column(name = "idfile")
    private Long idfile;
    @Column(name = "idimg")
    private Long idimg;
    @Column(name = "idown")
    private Long idOwn;
    @Column(name = "price")
    private Float price;
    @Column(name = "status")
    private String status;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGenre() {
        return genre;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getAbout() {
        return about;
    }

    public Long getIdfile() {
        return idfile;
    }

    public Long getIdimg() {
        return idimg;
    }

    public Long getIdOwn() {
        return idOwn;
    }

    public Float getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setIdfile(Long idfile) {
        this.idfile = idfile;
    }

    public void setIdimg(Long idimg) {
        this.idimg = idimg;
    }

    public void setIdOwn(Long idOwn) {
        this.idOwn = idOwn;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

