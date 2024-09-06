package com.marcpinol.gatewayservice.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class SecuredRoutesValidator {
    public static final List<String> openEndpoints = List.of(
            "/login"
    );

    public Predicate<String> isSecured =
            token -> openEndpoints
                    .stream()
                    .noneMatch(token::contains);
}
