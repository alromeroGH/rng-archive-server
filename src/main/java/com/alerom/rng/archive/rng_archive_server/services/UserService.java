package com.alerom.rng.archive.rng_archive_server.services;

import com.alerom.rng.archive.rng_archive_server.dto.request.update.PasswordUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.request.update.UserUpdateDTO;
import com.alerom.rng.archive.rng_archive_server.dto.response.UserResponseDTO;
import com.alerom.rng.archive.rng_archive_server.exceptions.InvalidPasswordException;
import com.alerom.rng.archive.rng_archive_server.exceptions.UserNotFoundException;
import com.alerom.rng.archive.rng_archive_server.mappers.UserMapper;
import com.alerom.rng.archive.rng_archive_server.models.User;
import com.alerom.rng.archive.rng_archive_server.repositories.UserRepository;
import com.alerom.rng.archive.rng_archive_server.security.JwtUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing user-related business logic, including
 * retrieving, updating, and validating user profiles and passwords.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs the UserService with the required dependencies.
     *
     * @param userRepository The repository for user data access.
     * @param userMapper The mapper for converting between user entities and DTOs.
     * @param passwordEncoder The encoder for user passwords.
     */
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves a user's profile based on their unique ID.
     *
     * @param id The ID of the user to retrieve.
     * @return A DTO containing the user's profile information.
     * @throws UserNotFoundException if the user with the specified ID does not exist.
     * @throws AccessDeniedException if the authenticated user is not authorized to access the requested profile.
     */
    public UserResponseDTO getUserProfile(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        User user = userOptional.orElseThrow(
                () -> new UserNotFoundException("User wit id " + id + " not found")
        );

        validateUserAccess(id);

        return userMapper.toResponseDTO(user);
    }

    /**
     * Updates a user's profile with new information from a DTO.
     *
     * @param id The ID of the user to update.
     * @param userUpdateDTO The DTO containing the updated user information.
     * @return A DTO of the updated user's profile.
     * @throws UserNotFoundException if the user with the specified ID does not exist.
     * @throws AccessDeniedException if the authenticated user is not authorized to update this profile.
     */
    @Transactional
    public UserResponseDTO updateUserProfile(Long id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User wit id " + id + " not found")
        );

        validateUserAccess(id);

        userMapper.updateEntityFromDTO(user, userUpdateDTO);

        userRepository.save(user);

        return userMapper.toResponseDTO(user);
    }

    /**
     * Updates a user's password after validating the current password.
     *
     * @param id The ID of the user whose password will be updated.
     * @param passwordUpdateDTO The DTO containing the current and new passwords.
     * @return A DTO of the user's profile after the password update.
     * @throws UserNotFoundException if the user with the specified ID does not exist.
     * @throws AccessDeniedException if the authenticated user is not authorized to update this password.
     * @throws InvalidPasswordException if the provided current password does not match the stored password.
     */
    @Transactional
    public UserResponseDTO updateUserPassword(Long id, PasswordUpdateDTO passwordUpdateDTO) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User wit id " + id + " not found")
        );

        validateUserAccess(id);

        if (!passwordEncoder.matches(passwordUpdateDTO.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("The current password provided is incorrect");
        }

        String newEncryptedPassword = passwordEncoder.encode(passwordUpdateDTO.getNewPassword());
        user.setPassword(newEncryptedPassword);

        userRepository.save(user);

        return userMapper.toResponseDTO(user);
    }

    /**
     * Internal method to validate if the current authenticated user has permission to access
     * the requested resource.
     *
     * @param requestedId The ID of the user being accessed.
     * @throws AccessDeniedException if the user does not have the required permissions.
     */
    private void validateUserAccess(Long requestedId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long authenticatedUserId = ((JwtUserDetails) authentication.getPrincipal()).getId();

        boolean isSameUser = authenticatedUserId.equals(requestedId);
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isSameUser && !isAdmin) {
            throw new AccessDeniedException("You are not authorized to access this resource.");
        }
    }
}