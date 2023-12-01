/**
 * Controller class for handling user sign-up and login operations.
 */
package com.ipsator.foodOrderingSystem.controller;


import com.ipsator.foodOrderingSystem.entity.User;
import com.ipsator.foodOrderingSystem.payload.Error;
import com.ipsator.foodOrderingSystem.payload.ApiResponse;
import com.ipsator.foodOrderingSystem.payload.JwtRequest;
import com.ipsator.foodOrderingSystem.payload.ServiceResponse;
import com.ipsator.foodOrderingSystem.dto.UserDto;
import com.ipsator.foodOrderingSystem.security.JwtHelper;
import com.ipsator.foodOrderingSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class SigninSignUpController {

  @Autowired
  private JwtHelper jwtHelper;
  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse> createUser(@RequestBody User user) {
    ServiceResponse<UserDto> createdUser = userService.createUser(user);
    if (createdUser.getSuccess()) {
      return new ResponseEntity<>((new ApiResponse("success", createdUser.getData(), null)),
          HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(
          (new ApiResponse("failure", null, new Error(createdUser.getMessage()))),
          HttpStatus.BAD_REQUEST);
    }

  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse> createToken(@RequestBody JwtRequest jwtRequest) {
    doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());
    final UserDetails user = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
    String token = jwtHelper.generateToken(user);
    ServiceResponse<UserDto> getUser = userService.getUser(jwtRequest.getEmail(),
        jwtRequest.getPassword());
    if (getUser.getSuccess()) {
      return new ResponseEntity<>((new ApiResponse("success",token, null)),
              HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>(
          (new ApiResponse("failure", null, new Error(getUser.getMessage()))),
          HttpStatus.BAD_REQUEST);
    }

  }

  public void doAuthenticate(String username, String password) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        username, password);
    authenticationManager.authenticate(authenticationToken);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public String exceptionHandler() {
    return "Bad Credentials!!!!!!!!!!!!!!";
  }

}
