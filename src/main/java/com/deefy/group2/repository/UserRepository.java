package com.deefy.group2.repository;


import com.deefy.group2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Interface para operações de banco de dados da entidade Usuario
public interface UserRepository extends JpaRepository<User, Long> {
    // Busca por email
    Optional<User> findByEmail(String email);
}

