package com.svalero.music.rights.controller;

import com.svalero.music.rights.dtos.LoginDto;
import com.svalero.music.rights.service.AuthService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto.getUsername(), loginDto.getPassword());
    }
}

