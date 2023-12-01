/**
 * Service implementation for user-related operations in the application.
 */
package com.ipsator.foodOrderingSystem.service;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import com.ipsator.foodOrderingSystem.entity.Address;
import com.ipsator.foodOrderingSystem.entity.User;
import com.ipsator.foodOrderingSystem.payload.ServiceResponse;
import com.ipsator.foodOrderingSystem.dto.UpdateUserDetailsDto;
import com.ipsator.foodOrderingSystem.dto.UserDto;
import com.ipsator.foodOrderingSystem.repository.AddressRepository;
import com.ipsator.foodOrderingSystem.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceimpl implements UserService {

  public static final String DEFAULT_ROLE = "ROLE_USER";

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepo userRepo;
  @Autowired
  private AddressRepository addressRepository;

  @Transactional
  @Override
  public ServiceResponse<UserDto> createUser(User user) {

    Optional<User> existingUserOpt = userRepo.findByEmail(user.getEmail());

    if (existingUserOpt.isEmpty()) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      user.setRole(DEFAULT_ROLE);

      List<Address> addresses= user.getAddress();
      for(Address address : addresses){
        Address saveAddress= addressRepository.save(address);

      }
      User savedUser = userRepo.save(user);

      UserDto userDto = new UserDto();
      userDto.setEmail(savedUser.getEmail());
      userDto.setFirstName(savedUser.getFirstName());
      userDto.setLastName(savedUser.getLastName());
      userDto.setGender(savedUser.getGender());
      userDto.setAddress(savedUser.getAddress());
      userDto.setId(savedUser.getId());
      userDto.setRole(savedUser.getRole());
      return new ServiceResponse<>(true, userDto,
          "Successfully created the user... PLEASE LOGIN!!!!!");
    } else {
      return new ServiceResponse<>(false, null, "The specified Email id is already present");
    }
  }
  @Override
  public ServiceResponse<UserDto> getUser(String email, String password) {

    Optional<User> userOpt = userRepo.findByEmail(email);

    if (userOpt.isPresent()) {
      User user = userOpt.get();
      if (passwordEncoder.matches(password, user.getPassword())) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setAddress(user.getAddress());
        userDto.setLastName(user.getLastName());
        userDto.setGender(user.getGender());
        userDto.setId(user.getId());
        return new ServiceResponse<>(true, userDto, "User logged in successfully.");

      } else {
        return new ServiceResponse<>(false, null, "Entered Password is wrong");
      }
    } else {
      return new ServiceResponse<>(false, null, "Entered Email ID does not exist");
    }
  }

  public User getLoggedInUser(String email) {
    return userRepo.findByEmail(email).get();
  }

  public static final String[] adminAccess = {"ROLE_ADMIN", "ROLE_USER"};

  public List<String> getRolesOfLoggedInUser(String email) {
    String roles = getLoggedInUser(email).getRole();
    List<String> assignRoles = Arrays.stream(roles.split(",")).collect(Collectors.toList());
    if (assignRoles.contains("ROLE_ADMIN")) {
      return Arrays.stream(adminAccess).collect(Collectors.toList());
    } else {
      return List.of(new String[]{"ROLE_USER"});
    }
  }

  public ServiceResponse<User> giveAccess(String email, String userRole, Principal principal) {
    Optional<User> user = userRepo.findByEmail(email);
    if (user.isPresent()) {

     List<String> activeRoles = getRolesOfLoggedInUser(email);
      String newRole = "";

      if (!(activeRoles.contains(userRole))) {

        newRole = user.get().getRole() + "," + userRole;
        user.get().setRole(newRole);
        userRepo.save(user.get());
        return new ServiceResponse<>(true, user.get(),
            "Hello!!! " + user.get().getEmail() + ". New Role has been assigned to you by "
                + principal.getName() + "i.e., " + user.get().getRole());

      } else {
        return new ServiceResponse<>(false, user.get(), "The user is already " + userRole);
      }

    }
    return new ServiceResponse<>(false, null, "cannot find the user with specified mail..!!!!");

  }

  @Override
  public List<UserDto> loadAll() {

    List<User> list = userRepo.findAll();
    List<UserDto> newList = new ArrayList<>();

    for (int i = 0; i < list.size(); i++) {

      newList.add(new UserDto(list.get(i)));

    }

    return newList;
  }

  @Override
  public ServiceResponse<UserDto> update(UpdateUserDetailsDto updateUserDetailsDto,
                                         Principal principal) {
    User loggedinUser = userRepo.findByEmail(principal.getName()).get();
    Optional<User> checkNewMail = userRepo.findByEmail(updateUserDetailsDto.getUpdatedMail());
    if (checkNewMail.isEmpty()) {
      loggedinUser.setEmail(updateUserDetailsDto.getUpdatedMail());
      loggedinUser.setFirstName(updateUserDetailsDto.getUpdatedFirstName());
      loggedinUser.setLastName(updateUserDetailsDto.getUpdatedLastName());
      loggedinUser.setPassword(passwordEncoder.encode(updateUserDetailsDto.getUpdatedPassword()));
      userRepo.save(loggedinUser);

    } else {
      return new ServiceResponse<>(false, null, "Given mail id is already present...!!!!");

    }
    return new ServiceResponse<>(true, new UserDto(updateUserDetailsDto),
        "The user details are successfully updated...");

  }

  @Override
  public ServiceResponse<UserDto> updateAnyUser(Integer id, UpdateUserDetailsDto updateUserDetailsDto,
      Principal principal) {
    Optional<User> updateDetailsOfUser = userRepo.findById(Long.valueOf(id));
    if(updateDetailsOfUser.isEmpty()){
      return new ServiceResponse<>(false, null, "Given id of the user is not present.PLEASE CHECK THE ID OF THE USER TO BE UPDATED..!!!!");
    }
    Optional<User> checkNewMail = userRepo.findByEmail(updateUserDetailsDto.getUpdatedMail());
    if (checkNewMail.isEmpty()) {
      updateDetailsOfUser.get().setEmail(updateUserDetailsDto.getUpdatedMail());
      updateDetailsOfUser.get().setFirstName(updateUserDetailsDto.getUpdatedFirstName());
      updateDetailsOfUser.get().setLastName(updateUserDetailsDto.getUpdatedLastName());
      updateDetailsOfUser.get().setPassword(passwordEncoder.encode(updateUserDetailsDto.getUpdatedPassword()));
      userRepo.save(updateDetailsOfUser.get());

    } else {
      return new ServiceResponse<>(false, null, "Given mail id to be updated is already present.TRY ANOTHER MAIL..!!!!");

    }
    return new ServiceResponse<>(true, new UserDto(updateUserDetailsDto),
        "The user details of "+updateDetailsOfUser.get().getEmail()+" are successfully updated by "+principal.getName());

  }


}
