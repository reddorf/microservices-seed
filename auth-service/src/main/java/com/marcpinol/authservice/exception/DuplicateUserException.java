package com.marcpinol.authservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DuplicateUserException extends RuntimeException {
    private final String userName;
}
