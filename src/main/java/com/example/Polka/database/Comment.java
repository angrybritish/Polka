package com.example.Polka.database;

import jakarta.persistence.*;

@Entity
@Table(name = "commentpolka")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "body")
    private String body;
    @Column(name = "time")
    private Long time;
    @Column(name = "idown")
    private Long idOwn;
    @Column(name = "idbook")
    private Long idBook;
    @Column(name = "raiting")
    private Long raiting;
    @Column(name = "deleted")
    private boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getters and setters for body
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    // Getters and setters for time
    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    // Getters and setters for idOwn
    public Long getIdOwn() {
        return idOwn;
    }

    public void setIdOwn(Long idOwn) {
        this.idOwn = idOwn;
    }

    // Getters and setters for idBook
    public Long getIdBook() {
        return idBook;
    }

    public void setIdBook(Long idBook) {
        this.idBook = idBook;
    }

    // Getters and setters for raiting
    public Long getRaiting() {
        return raiting;
    }

    public void setRaiting(Long raiting) {
        this.raiting = raiting;
    }

    // Getters and setters for deleted
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
