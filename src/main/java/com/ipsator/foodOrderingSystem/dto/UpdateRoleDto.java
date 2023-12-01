/**
 * Payload class representing a request to update a user's role in the application.
 */
package com.ipsator.foodOrderingSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleDto {

  private String email;

  private String role;
}
