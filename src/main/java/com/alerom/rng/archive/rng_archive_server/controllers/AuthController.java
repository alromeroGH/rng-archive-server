package com.alerom.rng.archive.rng_archive_server.controllers;

import com.alerom.rng.archive.rng_archive_server.exceptions.EmailAlreadyExistException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UidAlreadyExistException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UserNotFoundException;
import com.alerom.rng.archive.rng_archive_server.security.JwtResponse;
import com.alerom.rng.archive.rng_archive_server.security.LoginRequest;
import com.alerom.rng.archive.rng_archive_server.security.RegisterRequest;
import com.alerom.rng.archive.rng_archive_server.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class to manage authentication operations.
 * It provides endpoints for user registration and login.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private final AuthService authService;

    /**
     * Constructs the authController with the necessary service.
     * @param authService The service for user-related operations.
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handles user login requests.
     * Authenticates a user and returns a JWT token upon success.
     *
     * @param loginRequest The login request containing user credentials.
     * @return A ResponseEntity with the JWT token or an error message.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.login(loginRequest);

            return ResponseEntity.ok(jwtResponse);

        } catch (BadCredentialsException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

    /**
     * Handles user registration requests.
     * Creates a new user account with the provided details.
     *
     * @param registerRequest The registration request with user information.
     * @return A ResponseEntity confirming successful registration or an error message.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            authService.register(registerRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Bad credentials");
        } catch (EmailAlreadyExistException | UidAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
}