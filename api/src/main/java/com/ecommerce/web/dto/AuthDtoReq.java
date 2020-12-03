package com.ecommerce.web.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthDtoReq {

	@NotBlank
	private String email;
	
	@NotBlank
	private String password;

	public AuthDtoReq() {
	}
}
