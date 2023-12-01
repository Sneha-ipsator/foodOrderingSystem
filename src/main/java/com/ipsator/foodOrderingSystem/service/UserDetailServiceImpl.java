/**
 * Service implementation for loading user details by username in the application.
 */
package com.ipsator.foodOrderingSystem.service;
import com.ipsator.foodOrderingSystem.entity.User;
import com.ipsator.foodOrderingSystem.payload.CustomUserDetail;
import com.ipsator.foodOrderingSystem.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepo userRepo;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepo.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found!!"));
    CustomUserDetail customUserDetail = new CustomUserDetail(user);
    return customUserDetail;
  }


}
