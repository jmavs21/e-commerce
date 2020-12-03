package com.ecommerce.web.controller;

import javax.validation.Valid;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.model.User;
import com.ecommerce.service.IUserService;
import com.ecommerce.web.dto.ExpoPTDtoReq;

@RestController
@RequestMapping(value = "/expoPushTokens")
public class ExpoPushTokensController {
	
	private IUserService userService;

	public ExpoPushTokensController(IUserService userService) {
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
