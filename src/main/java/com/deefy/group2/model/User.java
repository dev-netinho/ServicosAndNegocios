package com.deefy.group2.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O novo banco usa SERIAL
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    @Setter
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    @Setter
    private String email;

    @Column(name = "senha", nullable = false, length = 100)
    @Setter
    private String password;

    // A MÁGICA DO RBAC AQUI: Ligando o usuário ao perfil
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "perfil_id")
    @Setter(AccessLevel.PROTECTED)
    private Perfil perfil;

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