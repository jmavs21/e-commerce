package com.ecommerce.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.service.impl.JwtUserDetailsService;
import com.ecommerce.web.dto.AuthDtoReq;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@PostMapping
	public String createAuthenticationToken(@RequestBody AuthDtoReq authDtoReq) throws Exception {
		return userDetailsService.getGeneratedToken(authDtoReq.getEmail());
	}
}
