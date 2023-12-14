package com.example.Polka.database;

import jakarta.persistence.*;

@Entity
@Table(name = "userspolka")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "mail")
    private String mail;
    @Column(name = "password")
    private String password;
    @Column(name = "deleted")
    private boolean deleted;
    @Column(name = "usertype")
    private int userType;

    public void setUserType(int userType) {
        this.userType = userType;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public void setDeleted(boolean active) {
        this.deleted = active;
    }
    public Long getId() {
        return id;
    }
    public String getMail() {
        return mail;
    }
    public String getUsername() {
        return username;
    }
    public String getPass() {
        return password;
    }
    public boolean getDeleted() {
        return deleted;
    }
    public int getUserType() {
        return userType;
    }

}

