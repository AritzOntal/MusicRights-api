package com.svalero.music.rights.service;

import com.svalero.music.rights.domain.User;
import com.svalero.music.rights.repository.UserRepository;
import com.svalero.music.rights.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<User> update(long id, User user) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User existingUser = optionalUser.get();
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setRole(user.getRole());
        existingUser.setMusician(user.getMusician());
        return ResponseEntity.ok(userRepository.save(existingUser));
    }

    public ResponseEntity<Void> delete(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<User> updateRole(long id, String role) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User existingUser = optionalUser.get();
        existingUser.setRole(role);
        return ResponseEntity.ok(userRepository.save(existingUser));
    }
}
