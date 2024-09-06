package com.marcpinol.gatewayservice.config;

import com.marcpinol.gatewayservice.service.JwtService;
import com.marcpinol.gatewayservice.service.SecuredRoutesValidator;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GatewayFilter {

    private final SecuredRoutesValidator securedRoutesValidator;
    private final JwtService jwtService;

    public AuthFilter(SecuredRoutesValidator securedRoutesValidator, JwtService jwtService) {
        this.securedRoutesValidator = securedRoutesValidator;
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = getToken(request);

        if (securedRoutesValidator.isSecured.test(token)) {
            if (!StringUtils.hasText(token)) {
                return this.getErrorResponse(exchange, HttpStatus.UNAUTHORIZED);
            }
            if (!jwtService.validateToken(token)) {
                return this.getErrorResponse(exchange, HttpStatus.FORBIDDEN);
            }
        }

        return chain.filter(exchange);
    }

    private String getToken(ServerHttpRequest request) {
        if (request.getHeaders().containsKey("Authorization")) {
            String bearerToken = request.getHeaders().getOrEmpty("Authorization").getFirst();

            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
        }

        return "";
    }

    private Mono<Void> getErrorResponse(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
