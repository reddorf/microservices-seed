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
            userService.create(new User().withUsername("admin").withPassword("admin").withAuthorities("ROLE_ADMIN"));
        };
    }

}
