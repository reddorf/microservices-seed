package com.marcpinol.authservice.controller;

import com.marcpinol.authservice.dto.JwtToken;
import com.marcpinol.authservice.dto.UserCredentials;
import com.marcpinol.authservice.model.User;
import com.marcpinol.authservice.service.AuthService;
import com.marcpinol.authservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public JwtToken login(@Valid @RequestBody UserCredentials userCredentials) {
        String token = authService.login(userCredentials.getUsername(), userCredentials.getPassword());
        User user = userService.findByUsername(userCredentials.getUsername());

        return new JwtToken()
                .withToken(token)
                .withRoles(user
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .withUsername(user.getUsername());
    }
}
