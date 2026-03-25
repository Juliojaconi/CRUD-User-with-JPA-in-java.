package br.com.krono.modelo;

import jakarta.persistence.*;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;
    @Column(name = "nome_usuario", nullable = false)
    private String nome;
    @Column(nullable = false, unique = true)
    private String email;


    public Usuario(String nome, String email) {

        this.nome = nome;
        this.email = email;
    }

    public Usuario() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
