package com.svalero.music.rights.controller;

import com.svalero.music.rights.domain.User;
import com.svalero.music.rights.service.AuthService;
import com.svalero.music.rights.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/v1/users")
    public ResponseEntity<User> create(@RequestBody User user) {
        authService.register(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/v1/users")
    public ResponseEntity<List<User>> findAll() {
        return userService.findAll();
    }
}
