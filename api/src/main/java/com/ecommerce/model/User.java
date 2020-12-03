package com.ecommerce.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Document(value = "users")
@Data
public class User implements UserDetails {

	private static final long serialVersionUID = -7547830018714464383L;

	@Id
	private String id;

	@NotBlank
	@Size(min = 2, max = 50)
	private String name;

	@NotBlank
	@Email
	@Size(min = 5, max = 255)
	@Indexed(unique = true)
	private String email;

	@NotBlank
	@Size(min = 3, max = 1024)
	private String password;

	private Boolean isAdmin = false;
	
	private String expoPushToken = "";

	public User() {
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	@Override
	public String getUsername() {
		return getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
