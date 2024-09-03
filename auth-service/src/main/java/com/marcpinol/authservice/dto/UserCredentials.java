package com.marcpinol.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public class UserCredentials {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
