package com.marcpinol.authservice.errors;

public record ValidationError(String field, String message) {}
