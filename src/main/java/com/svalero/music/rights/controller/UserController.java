package com.svalero.music.rights.controller;

import com.svalero.music.rights.domain.User;
import com.svalero.music.rights.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/v1/users")
    public ResponseEntity<User> create(@RequestBody @Valid String username, String password) {
        authService.register(username, password);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
