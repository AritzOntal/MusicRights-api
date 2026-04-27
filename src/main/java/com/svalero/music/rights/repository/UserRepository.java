package com.svalero.music.rights.repository;

import com.svalero.music.rights.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthService extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
