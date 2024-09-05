package com.marcpinol.authservice.service;

import com.marcpinol.authservice.exception.DuplicateUserException;
import com.marcpinol.authservice.exception.UserNotFoundException;
import com.marcpinol.authservice.model.User;
import com.marcpinol.authservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User get(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateUserException(user.getUsername());
        }

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
