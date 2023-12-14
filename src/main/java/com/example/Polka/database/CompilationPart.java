package com.example.Polka.database;

import jakarta.persistence.*;

@Entity
@Table(name = "compilationpartpolka")
public class CompilationPart {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "idcompilation")
    private Long idCompilation;
    @Column(name = "idbook")
    private Long idbook;
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

    public Long getIdCompilation() {
        return idCompilation;
    }

    public void setIdCompilation(Long idCompilation) {
        this.idCompilation = idCompilation;
    }

    public Long getIdbook() {
        return idbook;
    }

    public void setIdbook(Long idbook) {
        this.idbook = idbook;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
