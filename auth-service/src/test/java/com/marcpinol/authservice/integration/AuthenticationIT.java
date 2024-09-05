package com.marcpinol.authservice.integration;

import com.marcpinol.authservice.model.User;
import com.marcpinol.authservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthenticationIT {
    @Autowired
    private UserService userService;

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
    void shouldReturnTokenWhenCorrectCredentials() throws Exception {
        userService.create(new User().withUsername("usr").withPassword("$2a$12$CTlxj43wqejUbLdixrJGGuyKzlqU2okISp5vk2rGbJxcud2m8r0GG").withAuthorities("ROLE_USER"));

        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"usr\", \"password\": \"user\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void shouldNotReturnTokenAndReturnUnauthorizedWhenIncorrectCredentials() throws Exception {
        userService.create(new User().withUsername("usr").withPassword("$2a$12$CTlxj43wqejUbLdixrJGGuyKzlqU2okISp5vk2rGbJxcud2m8r0GG").withAuthorities("ROLE_USER"));

        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"usr\", \"password\": \"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    void shouldNotReturnTokenAndReturnUnauthorizedWhenUserDoesNotExist() throws Exception {
        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"usr\", \"password\": \"user\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    void shouldDenyAccessWhenExpiredToken() throws Exception {
        SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwt = jwt().jwt(j -> j.header("exp", "1")).authorities(new SimpleGrantedAuthority("ROLE_WHATEVER"));

        mvc.perform(get("/api/users").with(jwt))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/users/1").with(jwt))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/users").with(jwt))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAccessToLoginEndpoint() throws Exception {
        mvc.perform(post("/api/login"))
                .andExpect(status().isBadRequest()); // We don't care that the actual POST fails, only that it's going through
    }

    @Test
    void shouldDenyAccessToOtherEndpoints() throws Exception {
        mvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/users/1"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/api/users"))
                .andExpect(status().isUnauthorized());
    }
}