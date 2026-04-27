package com.svalero.music.rights.controller;

import com.svalero.music.rights.domain.User;
import com.svalero.music.rights.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService authService) {
        this.userService = authService;
    }

    @PostMapping("/v1/users")
    public ResponseEntity<User> create(@RequestBody @Valid User user) {
        userService.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
