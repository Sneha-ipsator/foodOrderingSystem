/**
 * Controller class for managing user-related operations.
 */
package com.ipsator.foodOrderingSystem.controller;
import java.security.Principal;
import java.util.List;

import com.ipsator.foodOrderingSystem.dto.UpdateRoleDto;
import com.ipsator.foodOrderingSystem.dto.UpdateUserDetailsDto;
import com.ipsator.foodOrderingSystem.dto.UserDto;
import com.ipsator.foodOrderingSystem.payload.*;

import com.ipsator.foodOrderingSystem.entity.User;
import com.ipsator.foodOrderingSystem.payload.Error;
import com.ipsator.foodOrderingSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@EnableMethodSecurity
public class UserController {

  @Autowired
  private UserService userService;

  @PutMapping("/access")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Secured("ROLE_ADMIN")
  public ResponseEntity<ApiResponse> giveAccessToUser(@RequestBody UpdateRoleDto updateRoleDto,
                                                      Principal principal) {
    ServiceResponse<User> getUser = userService.giveAccess(updateRoleDto.getEmail(),
        updateRoleDto.getRole(), principal);
    if (getUser.getSuccess()) {
      return new ResponseEntity<>(
          (new ApiResponse("success", new UserDto(getUser.getData()), null)), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          (new ApiResponse("failure", null, new Error(getUser.getMessage()))),
          HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/fetchAllUsers")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Secured("ROLE_ADMIN")
  public List<UserDto> loadUsers() {
    return userService.loadAll();
  }

  @PutMapping("/updateOwnDetails")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<ApiResponse> updateUserDetail(
          @RequestBody UpdateUserDetailsDto updateUserDetailsDto, Principal principal) {
    ServiceResponse<UserDto> getUser = userService.update(updateUserDetailsDto, principal);
    if (getUser.getSuccess()) {
      return new ResponseEntity<>((new ApiResponse("success", getUser.getData(), null)),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          (new ApiResponse("failure", null, new Error(getUser.getMessage()))),
          HttpStatus.BAD_REQUEST);
    }
  }


  @PutMapping("/update")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<ApiResponse> updateAnyUserDetail(@RequestParam Integer id, @RequestBody UpdateUserDetailsDto updateUserDetailsDto, Principal principal) {
    ServiceResponse<UserDto> getUser =userService.updateAnyUser(id,updateUserDetailsDto, principal);
    if(getUser.getSuccess()){
      return new ResponseEntity<>((new ApiResponse("success",getUser.getData(),null)), HttpStatus.OK);
    }else{
      return new ResponseEntity<>((new ApiResponse("failure",null, new Error(getUser.getMessage()))), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/getName")
  @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
  public String giveName(Principal principal) {
    return principal.getName();
  }




}




