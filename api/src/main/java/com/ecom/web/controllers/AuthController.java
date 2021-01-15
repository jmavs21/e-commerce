package com.ecom.web.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.config.jwt.JwtTokenUtil;
import com.ecom.domain.users.User;
import com.ecom.domain.users.UserService;
import com.ecom.error.BadRequestException;
import com.ecom.web.dto.AuthDtoReq;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

  private UserService userService;
  private JwtTokenUtil jwtTokenUtil;
  private PasswordEncoder passwordEncoder;

  public AuthController(
      UserService userService, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.jwtTokenUtil = jwtTokenUtil;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping
  public String create(@RequestBody AuthDtoReq authDtoReq) throws Exception {
    User user = userService.findByEmail(authDtoReq.getEmail());
    if (!passwordEncoder.matches(authDtoReq.getPassword(), user.getPassword()))
      throw new BadRequestException("incorrect email or password");
    return jwtTokenUtil.generateToken(user);
  }
}
