package com.alerom.rng.archive.rng_archive_server.controllers;

import com.alerom.rng.archive.rng_archive_server.dto.request.update.PasswordUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.UserUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.UserResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.InvalidPasswordException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UserNotFoundException;
import com.alerom.rng.archive.rng_archive_server.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing user-related operations.
 * It provides endpoints to retrieve, update, and manage user profiles.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    /**
     * Constructs the UserController with the necessary service.
     * @param userService The service for user profile management.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a user's profile by their unique ID.
     *
     * @param id The ID of the user to retrieve.
     * @return A ResponseEntity containing the user's profile data or an error message if not found or access is denied.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        try {
            UserResponseDTO userResponseDTO = userService.getUserProfile(id);

            return ResponseEntity.ok(userResponseDTO);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * Updates a user's profile with new information.
     *
     * @param id The ID of the user to update.
     * @param userUpdateDTO The DTO containing the new user information.
     * @return A ResponseEntity with the updated user's profile data or an error message if not found or access is denied.
     */
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id,@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            UserResponseDTO userResponseDTO = userService.updateUserProfile(id, userUpdateDTO);

            return ResponseEntity.ok(userResponseDTO);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * Updates a user's password.
     *
     * @param id The ID of the user whose password will be updated.
     * @param passwordUpdateDTO The DTO containing the current and new passwords.
     * @return A ResponseEntity confirming the password update or an error message if the user is not found, the password is invalid, or access is denied.
     */
    @PostMapping("/update/password/{id}")
    public ResponseEntity<?> updateUserPassword(@PathVariable Long id,@Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        try {
            UserResponseDTO userResponseDTO = userService.updateUserPassword(id, passwordUpdateDTO);

            return ResponseEntity.ok(userResponseDTO);
        } catch (UserNotFoundException | InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }
}