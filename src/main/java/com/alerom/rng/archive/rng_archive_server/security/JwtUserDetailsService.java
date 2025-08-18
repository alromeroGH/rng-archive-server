package com.alerom.rng.archive.rng_archive_server.security;

import com.alerom.rng.archive.rng_archive_server.models.Users;
import com.alerom.rng.archive.rng_archive_server.repositories.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom implementation of the UserDetailsService interface.
 * This service is responsible for loading user-specific data from the database
 * during the authentication process.
 *
 * @author Alejo Romero
 * @version 1.0
 * @see org.springframework.security.core.userdetails.UserDetailsService
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    /**
     * Constructs the JwtUserDetailsService with the UsersRepository dependency.
     * @param usersRepository The repository for user data access.
     */
    public JwtUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Locates the user based on the username (email) for authentication.
     * This method is called by the AuthenticationManager during the login process.
     *
     * @param username The username (email) of the user to retrieve.
     * @return A UserDetails object that Spring Security can use for authentication.
     * @throws UsernameNotFoundException if the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new JwtUserDetails(user);
    }
}