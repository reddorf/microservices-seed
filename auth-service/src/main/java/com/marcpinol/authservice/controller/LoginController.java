package com.marcpinol.authservice.controller;

import com.marcpinol.authservice.dto.JwtToken;
import com.marcpinol.authservice.dto.UserCredentials;
import com.marcpinol.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public JwtToken login(@Valid @RequestBody UserCredentials userCredentials) {
        String token = authService.login(userCredentials.getUsername(), userCredentials.getPassword());
        return new JwtToken().withToken(token);
    }
}
