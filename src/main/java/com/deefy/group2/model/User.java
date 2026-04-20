package com.deefy.group2.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
public class User implements UserDetails {

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

    public User() {
    }

    public User(Long id, String name, String email, String password, Perfil perfil) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.perfil = perfil;
    }

    public User(String name, String email, String password, Perfil perfil) {
        this(null, name, email, password, perfil);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // prefixo "ROLE_" para usar hasRole()
        return List.of(new SimpleGrantedAuthority("ROLE_" + perfil.getNome().toUpperCase()));
    }

    @Override
    public String getUsername() {
        // No Deefy, o email é o nosso identificador único de login
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    @Override
    // A conta não expirou?
    public boolean isAccountNonExpired() { return true; }

    @Override
    // A conta não está bloqueada?
    public boolean isAccountNonLocked() { return true; }

    @Override
    // A senha não expirou?
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    // A conta não está ativa?
    public boolean isEnabled() { return true; }
}
