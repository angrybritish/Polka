package com.example.Polka.database;

import jakarta.persistence.*;

@Entity
@Table(name = "questionpolka")
public class Question {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idown")
    private Long idOwn;

    @Column(name = "question")
    private String question;

    @Column(name = "response")
    private String response;

    @Column(name = "time")
    private Long time;

    @Column(name = "status")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdOwn() {
        return idOwn;
    }

    public void setIdOwn(Long idOwn) {
        this.idOwn = idOwn;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
