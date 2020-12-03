package com.ecommerce.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {

	@JsonProperty("id")
	private String id;

	@NotBlank
	@Size(min = 2, max = 50)
	private String name;

	@NotBlank
	@Email
	@Size(min = 5, max = 255)
	private String email;

	private Boolean isAdmin = false;
	
	private String expoPushToken = "";

	public UserDto() {
	}
}
