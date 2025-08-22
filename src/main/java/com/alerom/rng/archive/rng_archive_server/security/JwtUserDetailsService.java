package com.alerom.rng.archive.rng_archive_server.security;

import com.alerom.rng.archive.rng_archive_server.models.User;
import com.alerom.rng.archive.rng_archive_server.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    private final UserRepository userRepository;

    /**
     * Constructs the JwtUserDetailsService with the UsersRepository dependency.
     * @param userRepository The repository for user data access.
     */
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new JwtUserDetails(user);
    }
}