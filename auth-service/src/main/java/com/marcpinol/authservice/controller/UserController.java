package com.marcpinol.authservice.controller;

import com.marcpinol.authservice.dto.UserRequest;
import com.marcpinol.authservice.dto.UserResponse;
import com.marcpinol.authservice.model.User;
import com.marcpinol.authservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        User user = userService.get(id);
        return new UserResponse().withId(user.getId()).withUsername(user.getUsername()).withRoles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getAll().stream().map(user -> new UserResponse()
                        .withId(user.getId())
                        .withUsername(user.getUsername()).withRoles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()))
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody UserRequest userRequest) {
        User user = new User().withUsername(userRequest.getUsername())
                .withPassword(userRequest.getPassword())
                .withAuthorities(String.join(";", userRequest.getRoles()));
        User createdUser = userService.create(user);
        return new UserResponse()
                .withId(createdUser.getId())
                .withUsername(createdUser.getUsername())
                .withRoles(createdUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    }
}
