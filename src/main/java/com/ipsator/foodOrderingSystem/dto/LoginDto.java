/**
 * Payload class representing a login request in the application.
 */
package com.ipsator.foodOrderingSystem.dto;

import lombok.Data;

@Data
public class LoginDto {


  private String email;

  private String password;

}
