package com.alerom.rng.archive.rng_archive_server.security;

import com.alerom.rng.archive.rng_archive_server.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Custom implementation of the UserDetails interface.
 * This record provides the core user information required by Spring Security
 * for authentication and authorization. It acts as an adapter for the Users entity.
 *
 * @author Alejo Romero
 * @version 1.0
 */
public record JwtUserDetails(User user) implements UserDetails {

    /**
     * Returns the authorities (roles) granted to the user.
     * The roles are prefixed with "ROLE_" as required by Spring Security.
     *
     * @return A collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getIsAdmin()) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    /**
     * Returns the user's password.
     *
     * @return The user's password from the User entity.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the unique username used for authentication, which is the user's email.
     *
     * @return The user's email address.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Returns the user's id.
     *
     * @return The user's id from the User entity.
     */
    public Long getId() {
        return user.getId();
    }
}