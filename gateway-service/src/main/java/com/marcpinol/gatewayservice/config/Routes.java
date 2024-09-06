package com.marcpinol.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class Routes {
    private final AuthFilter authFilter;

    public Routes(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("login", r -> r.method(HttpMethod.POST)
                        .and().path("/api/login")
                        .uri("lb://auth-service"))
                .route("users", r -> r.path("/api/users/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://auth-service"))
                .build();
    }
}
