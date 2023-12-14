package com.example.Polka.database;

import jakarta.persistence.*;

@Entity
@Table(name = "orderspolka")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idown")
    private Long idOwn;

    @Column(name = "idbook")
    private Long idbook;

    @Column(name = "code")
    private Long code;

    @Column(name = "price")
    private Float price;

    @Column(name = "status")
    private String status;

    public Long getId() {
        return id;
    }

    public Long getIdOwn() {
        return idOwn;
    }

    public Long getIdBook() {
        return idbook;
    }

    public Long getCode() {
        return code;
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

    public void setIdOwn(Long idOwn) {
        this.idOwn = idOwn;
    }

    public void setIdBook(Long idbook) {
        this.idbook = idbook;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
