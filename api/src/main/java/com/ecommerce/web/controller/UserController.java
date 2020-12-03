package com.ecommerce.web.controller;

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

import com.ecommerce.model.User;
import com.ecommerce.service.IUserService;
import com.ecommerce.web.dto.UserDto;
import com.ecommerce.web.dto.UserDtoReq;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	private IUserService userService;
	private ModelMapper modelMapper;

	public UserController(IUserService userService, ModelMapper modelMapper) {
		this.userService = userService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public Collection<UserDto> findAll() {
		Iterable<User> users = userService.findAll();
		List<UserDto> userDtos = new ArrayList<>();
		users.forEach(u -> userDtos.add(entityToDto(u)));
		return userDtos;
	}

	@GetMapping(value = "/{id}")
	public UserDto findOne(@PathVariable String id) {
		User user = userService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
		User user = userService.create(dtoToEntity(newUser));
		String token = userService.getTokenFromEmail(user.getEmail());
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-auth-token", token);
		headers.set("access-control-expose-headers", "x-auth-token");
		return ResponseEntity.ok().headers(headers).body(entityToDto(user));
	}

	@PutMapping(value = "/{id}")
	public UserDto updateUser(@PathVariable("id") String id, @Valid @RequestBody UserDtoReq updatedUser) {
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
