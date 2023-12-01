/**
 * Repository interface for managing User entities in the application.
 */
package com.ipsator.foodOrderingSystem.repository;

import java.util.Optional;

import com.ipsator.foodOrderingSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface extending JpaRepository to perform database operations on User entities.
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long> {

  /**
   * Retrieves a user by their email address.
   *
   * @param email The email address of the user to find.
   * @return An Optional containing the User entity if found, or an empty Optional if not found.
   */
  @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
  Optional<User> findByEmail(@Param("email") String email);

}
