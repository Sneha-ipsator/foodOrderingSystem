/**
 * Payload class representing a JWT authentication request in the application.
 */
package com.ipsator.foodOrderingSystem.payload;

import lombok.Data;

/**
 * Represents a JSON Web Token (JWT) authentication request with email and password.
 */
@Data
public class JwtRequest {

  /**
   * The email address of the user.
   */
  private String email;

  /**
   * The password associated with the user's account.
   */
  private String password;

}
