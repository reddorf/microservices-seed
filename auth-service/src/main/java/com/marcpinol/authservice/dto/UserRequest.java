package com.marcpinol.authservice.dto;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;

@GroupSequence({UserRequest.class, UserRequest.ExtendedValidation.class})
@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class UserRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotEmpty
    private List<String> roles;

    @AssertTrue(message = "Passwords must be equal", groups = ExtendedValidation.class)
    public boolean isEqualPassword() {
        return password.equals(repeatPassword);
    }

    public interface ExtendedValidation {
    }
}
