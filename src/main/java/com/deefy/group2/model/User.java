package com.deefy.group2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O novo banco usa SERIAL
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "senha", nullable = false, length = 100)
    private String password;

    // A MÁGICA DO RBAC AQUI: Ligando o usuário ao perfil
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    protected User() {}

    // O construtor agora recebe o Perfil em vez de uma String
    public User(String name, String email, String password, Perfil perfil) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.perfil = perfil;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Perfil getPerfil() { return perfil; }
}