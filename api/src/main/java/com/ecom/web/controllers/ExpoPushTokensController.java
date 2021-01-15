package com.ecom.web.controllers;

import javax.validation.Valid;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.domain.users.User;
import com.ecom.domain.users.UserService;
import com.ecom.web.dto.ExpoPTDtoReq;

@RestController
@RequestMapping(value = "/expoPushTokens")
public class ExpoPushTokensController {

  private UserService userService;

  public ExpoPushTokensController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(Authentication authentication, @Valid @RequestBody ExpoPTDtoReq newExpoPT) {
    if (authentication.getPrincipal() == null)
      throw new DataRetrievalFailureException("User not found.");
    User user = (User) authentication.getPrincipal();
    user.setExpoPushToken(newExpoPT.getToken());
    userService.save(user);
  }
}
