package com.ecom.web.controllers;

import static com.ecom.config.jwt.JwtRequestFilter.X_AUTH_TOKEN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ecom.config.jwt.JwtTokenUtil;
import com.ecom.domain.users.User;
import com.ecom.domain.users.UserService;
import com.ecom.web.dto.UserDto;
import com.ecom.web.dto.UserDtoReq;

@RestController
@RequestMapping(value = "/users")
public class UserController {

  private UserService userService;
  private JwtTokenUtil jwtTokenUtil;
  private PasswordEncoder passwordEncoder;
  private ModelMapper modelMapper;

  public UserController(
      UserService userService,
      JwtTokenUtil jwtTokenUtil,
      PasswordEncoder passwordEncoder,
      ModelMapper modelMapper) {
    this.userService = userService;
    this.jwtTokenUtil = jwtTokenUtil;
    this.passwordEncoder = passwordEncoder;
    this.modelMapper = modelMapper;
  }

  @GetMapping
  public Collection<UserDto> findAll() {
    Iterable<User> users = userService.findAll();
    List<UserDto> userDtos = new ArrayList<>();
    for (User user : users) {
      userDtos.add(entityToDto(user));
    }
    return userDtos;
  }

  @GetMapping(value = "/{id}")
  public UserDto findOne(@PathVariable String id) {
    User user =
        userService
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return entityToDto(user);
  }

  @GetMapping(value = "/me")
  public UserDto findOne(Authentication authentication) {
    if (authentication.getPrincipal() == null)
      throw new DataRetrievalFailureException("User not found.");
    return entityToDto((User) authentication.getPrincipal());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<UserDto> create(@Valid @RequestBody UserDtoReq newUser) {
    User user =
        userService.create(dtoToEntity(newUser), passwordEncoder.encode(newUser.getPassword()));
    String token = jwtTokenUtil.generateToken(user);
    HttpHeaders headers = new HttpHeaders();
    headers.set(X_AUTH_TOKEN, token);
    headers.set("access-control-expose-headers", X_AUTH_TOKEN);
    return ResponseEntity.ok().headers(headers).body(entityToDto(user));
  }

  @PutMapping(value = "/{id}")
  public UserDto updateUser(
      @PathVariable("id") String id, @Valid @RequestBody UserDtoReq updatedUser) {
    return entityToDto(userService.save(dtoToEntity(updatedUser)));
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") String id) {
    userService.delete(id);
  }

  protected UserDto entityToDto(User entity) {
    return modelMapper.map(entity, UserDto.class);
  }

  protected User dtoToEntity(UserDtoReq dto) {
    return modelMapper.map(dto, User.class);
  }
}
