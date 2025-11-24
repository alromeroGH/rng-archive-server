package com.alerom.rng.archive.rng_archive_server.services;

import com.alerom.rng.archive.rng_archive_server.exceptions.EmailAlreadyExistException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UidAlreadyExistException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UserNotFoundException;
import com.alerom.rng.archive.rng_archive_server.models.User;
import com.alerom.rng.archive.rng_archive_server.repositories.UserRepository;
import com.alerom.rng.archive.rng_archive_server.security.*;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for handling authentication-related business logic, including
 * login and registration.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructs the UsersService with the required dependencies.
     *
     * @param userRepository The repository for user data access.
     * @param passwordEncoder The encoder for user passwords.
     * @param jwtUtil The utility class for JWT operations.
     * @param authenticationManager The manager for user authentication.
     */

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticates a user based on provided login credentials and generates a JWT token.
     *
     * @param loginRequest The login request containing user email and password.
     * @return A JwtResponse containing the generated token and user details.
     * @throws UserNotFoundException if the user is not found.
     * @throws BadCredentialsException if the password is incorrect.
     */
    @Transactional
    public JwtResponse login(LoginRequest loginRequest) {
        Optional<User> optionalUsers = userRepository.findByEmail(loginRequest.getEmail());

        if (optionalUsers.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        JwtUserDetails user = (JwtUserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(authentication);
        return new JwtResponse(jwt, user.user().getId(), user.user().getUserName(), user.user().getIsAdmin());
    }

    /**
     * Registers a new user with the provided details.
     * Encodes the password before saving the user to the database.
     *
     * @param registerRequest The registration request containing user details.
     * @throws EmailAlreadyExistException if the email is already in use.
     * @throws UidAlreadyExistException if the UID is already in use.
     */
    @Transactional
    public void register(RegisterRequest registerRequest) {
        Boolean userEmail = userRepository.existsByEmail(registerRequest.getEmail());
        Boolean userUid = userRepository.existsByUid(registerRequest.getUid());

        if (userEmail) {
            throw new EmailAlreadyExistException("Email already exist");
        } else if (userUid) {
            throw new UidAlreadyExistException("UID already exist");
        }

        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setUserName(registerRequest.getUserName());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setUid(registerRequest.getUid());
        newUser.setIsAdmin(false);
        newUser.setIsDeleted(false);

        userRepository.save(newUser);
    }
}