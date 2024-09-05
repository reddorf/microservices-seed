package com.marcpinol.authservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
public class AuthorizationIT {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void shouldAllowAccessToAdmin() throws Exception {
        SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwt = jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"));

        mvc.perform(get("/api/users").with(jwt))
                .andExpect(status().isOk());
        mvc.perform(get("/api/users/1").with(jwt))
                .andExpect(status().isOk());
        mvc.perform(post("/api/users")
                        .with(jwt))
                .andExpect(status().isBadRequest()); // We don't care that the actual POST fails, only that it's going through
    }

    @Test
    void shouldFilterAccessToViewer() throws Exception {
        SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwt = jwt().authorities(new SimpleGrantedAuthority("ROLE_VIEWER"));

        mvc.perform(get("/api/users").with(jwt))
                .andExpect(status().isOk());
        mvc.perform(get("/api/users/1").with(jwt))
                .andExpect(status().isOk());
        mvc.perform(post("/api/users")
                        .with(jwt))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldDenyAccessToOthers() throws Exception {
        SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwt = jwt().authorities(new SimpleGrantedAuthority("ROLE_WHATEVER"));

        mvc.perform(get("/api/users").with(jwt))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/users/1").with(jwt))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/users")
                        .with(jwt))
                .andExpect(status().isForbidden());
    }
}