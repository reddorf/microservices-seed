package com.marcpinol.authservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@With
public class User implements UserDetails {
    private static final String AUTHORITIES_DELIMITER = ";";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "USER_NAME", unique = true, nullable = false)
    private String username;
    @Setter
    @Column(name = "PASSWORD", unique = false, nullable = false)
    private String password;
    @Column(name = "AUTHORITIES", unique = false, nullable = false)
    private String authorities;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(this.authorities.split(AUTHORITIES_DELIMITER))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
