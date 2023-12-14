package com.example.Polka.database;

import jakarta.persistence.*;

@Entity
@Table(name = "filespolka")
public class File {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "realname")
    private String realName;

    @Column(name = "genname")
    private String genName;

    @Column(name = "time")
    private Long time;

    @Column(name = "type")
    private String type;

    @Column(name = "size")
    private Long size;

    @Column(name = "idown")
    private Long idOwn;

    @Column(name = "idbook")
    private Long idBook;

    @Column(name = "deleted")
    private Boolean deleted;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }

    public void setGenName(String genName) {
        this.genName = genName;
    }

    public String getGenName() {
        return genName;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTime() {
        return time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getSize() {
        return size;
    }

    public void setIdOwn(Long idOwn) {
        this.idOwn = idOwn;
    }

    public Long getIdOwn() {
        return idOwn;
    }

    public void setIdBook(Long idBook) {
        this.idBook = idBook;
    }

    public Long getIdBook() {
        return idBook;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getDeleted() {
        return deleted;
    }
}