package com.example.Polka.database;
import jakarta.persistence.*;

@Entity
@Table(name = "savedpolka")
public class Saved {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idown")
    private Long idOwn;

    @Column(name = "idbook")
    private Long idBook;

    @Column(name = "time")
    private Long time;

    @Column(name = "status")
    private String status;

    @Column(name = "deleted")
    private boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // ID Owner
    public Long getIdOwn() {
        return idOwn;
    }

    public void setIdOwn(Long idOwn) {
        this.idOwn = idOwn;
    }

    // ID Book
    public Long getIdBook() {
        return idBook;
    }

    public void setIdBook(Long idBook) {
        this.idBook = idBook;
    }

    // Time
    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    // Status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Deleted
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
