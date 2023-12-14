package com.example.Polka.database;

import jakarta.persistence.*;

@Entity
@Table(name = "compilationpolka")
public class Compilation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "idown")
    private Long idOwn;
    @Column(name = "time")
    private Long time;
    @Column(name = "deleted")
    private boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
    public Long getIdOwn() {
        return idOwn;
    }
    public void setIdOwn(Long idOwn) {
        this.idOwn = idOwn;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
