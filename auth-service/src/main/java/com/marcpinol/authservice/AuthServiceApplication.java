package com.marcpinol.authservice;

import com.marcpinol.authservice.model.User;
import com.marcpinol.authservice.service.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthServiceApplication {
    private final UserService userService;

    public AuthServiceApplication(UserService userService) {
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    public ApplicationRunner devApplicationRunner() {
        // Load initial data into the database
        return arg -> {
            userService.create(new User().withUsername("admin").withPassword("$2a$12$t3uTcoMrQIQDBPOMgSxF/.c/oCKZ9H.dzgd4v.X224LHLavgVLC.2").withAuthorities("ROLE_ADMIN"));
            userService.create(new User().withUsername("user").withPassword("$2a$12$CTlxj43wqejUbLdixrJGGuyKzlqU2okISp5vk2rGbJxcud2m8r0GG").withAuthorities("ROLE_VIEWER"));
        };
    }

}
