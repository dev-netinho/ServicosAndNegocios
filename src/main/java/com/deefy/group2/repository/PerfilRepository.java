package com.deefy.group2.repository;

import com.deefy.group2.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Interface para operações de banco de dados da entidade Perfil
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    // Busca um perfil pelo seu nome exato
    Optional<Perfil> findByNome(String nome);
}
