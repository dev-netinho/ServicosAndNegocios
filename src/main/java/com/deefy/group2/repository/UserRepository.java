package com.deefy.group2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deefy.group2.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
