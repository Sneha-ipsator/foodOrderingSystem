/**
 * Payload class representing a request to update user details in the application.
 */
package com.ipsator.foodOrderingSystem.dto;

//import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request to update user details, including email, password, first name, last name,
 * and gender.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDetailsDto {

  private String updatedMail;

  private String updatedPassword;

  private String updatedFirstName;

  private String updatedLastName;

  private String updatedGender;

  private String updatedAddress;


}
