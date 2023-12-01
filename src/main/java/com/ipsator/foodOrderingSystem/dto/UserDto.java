/**
 * Payload class representing user details in the application.
 */
package com.ipsator.foodOrderingSystem.dto;
import com.ipsator.foodOrderingSystem.dto.UpdateUserDetailsDto;
import com.ipsator.foodOrderingSystem.entity.Address;
import com.ipsator.foodOrderingSystem.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private Long id;

  private String email;

  private String firstName;

  private String lastName;

  private String gender;

  private String role;

  private List<Address> address;
  public UserDto(User user) {

    this.id = user.getId();
    this.email = user.getEmail();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.gender = user.getGender();
    this.role = user.getRole();
    this.address=user.getAddress();

  }
  public UserDto(UpdateUserDetailsDto user) {
    this.email = user.getUpdatedMail();
    this.firstName = user.getUpdatedFirstName();
    this.lastName = user.getUpdatedLastName();
    this.gender = user.getUpdatedGender();

  }

}
