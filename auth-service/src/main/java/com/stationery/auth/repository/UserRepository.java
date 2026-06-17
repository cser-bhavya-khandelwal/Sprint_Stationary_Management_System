package com.stationery.auth.repository;

import com.stationery.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing User persistence.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Finds a user by their email address.
     *
     * @param email user email
     * @return Optional containing the User if found, empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email address.
     *
     * @param email user email
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
}
