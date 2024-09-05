package com.marcpinol.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class JwtToken {
    private String token;
    private String username;
    private List<String> roles;
}
