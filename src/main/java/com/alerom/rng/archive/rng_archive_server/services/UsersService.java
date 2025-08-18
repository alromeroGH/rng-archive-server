package com.alerom.rng.archive.rng_archive_server.services;

import com.alerom.rng.archive.rng_archive_server.exceptions.EmailAlreadyExistException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UidAlreadyExistException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UserNotFoundException;
import com.alerom.rng.archive.rng_archive_server.models.Users;
import com.alerom.rng.archive.rng_archive_server.repositories.UsersRepository;
import com.alerom.rng.archive.rng_archive_server.security.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for handling user-related business logic, including
 * authentication, registration, and data management.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructs the UsersService with the required dependencies.
     *
     * @param usersRepository The repository for user data access.
     * @param passwordEncoder The encoder for user passwords.
     * @param jwtUtil The utility class for JWT operations.
     * @param authenticationManager The manager for user authentication.
     */

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.usersRepository = usersRepository;
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
    public JwtResponse login(LoginRequest loginRequest) {
        Optional<Users> optionalUsers = usersRepository.findByEmail(loginRequest.getEmail());

        if (optionalUsers.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);
        JwtUserDetails user = (JwtUserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(authentication);
        return new JwtResponse(jwt, user.user().getId(), user.user().getUserName());
    }

    /**
     * Registers a new user with the provided details.
     * Encodes the password before saving the user to the database.
     *
     * @param registerRequest The registration request containing user details.
     * @throws EmailAlreadyExistException if the email is already in use.
     * @throws UidAlreadyExistException if the UID is already in use.
     */
    public void register(RegisterRequest registerRequest) {
        Boolean userEmail = usersRepository.existsByEmail(registerRequest.getEmail());
        Boolean userUid = usersRepository.existsByUid(registerRequest.getUid());

        if (userEmail) {
            throw new EmailAlreadyExistException("Email already exist");
        } else if (userUid) {
            throw new UidAlreadyExistException("UID already exist");
        }

        Users newUser = new Users();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setUserName(registerRequest.getUserName());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setUid(registerRequest.getUid());
        newUser.setIsAdmin(false);

        usersRepository.save(newUser);
    }
}