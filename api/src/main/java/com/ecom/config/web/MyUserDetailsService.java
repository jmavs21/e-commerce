package com.ecom.config.web;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecom.domain.users.UserService;

/** AuthenticationManager will use this method to fetch the user. */
@Service
public class MyUserDetailsService implements UserDetailsService {

  private UserService userService;

  public MyUserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userService.findByEmail(email);
  }
}
