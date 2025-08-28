package com.alerom.rng.archive.rng_archive_server.repositories;

import com.alerom.rng.archive.rng_archive_server.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User data.
 * Extends JpaRepository to provide standard CRUD operations.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their unique email address.
     *
     * @param email The email address to search for.
     * @return An Optional containing the found user, or an empty Optional if not found.
     */
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isDeleted = false")
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * Finds a user by their unique identifier (ID).
     *
     * @param id The user's unique ID.
     * @return An Optional containing the found user, or an empty Optional if not found.
     */
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.isDeleted = false")
    Optional<User> findById(@Param("id") Long id);

    /**
     * Checks if a user with the given email address already exists.
     *
     * @param email The email address to check.
     * @return True if a user with this email exists, false otherwise.
     */
    Boolean existsByEmail(String email);

    /**
     * Checks if a user with the given UID (unique identifier) already exists.
     *
     * @param uid The UID to check.
     * @return True if a user with this UID exists, false otherwise.
     */
    Boolean existsByUid(String uid);
}