package com.marcpinol.authservice.errors;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorMsg(HttpStatus status, String message, List<ValidationError> errors){}
