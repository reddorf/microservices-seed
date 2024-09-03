package com.marcpinol.authservice.controller;

import com.marcpinol.authservice.dto.UserCredentials;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {
    @PostMapping("/login")
    public String login(@Valid @RequestBody UserCredentials userCredentials) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
