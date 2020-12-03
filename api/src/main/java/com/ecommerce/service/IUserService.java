package com.ecommerce.service;

import java.util.Optional;

import com.ecommerce.model.User;

public interface IUserService {

	Optional<User> findById(String id);

	User save(User user);

	Iterable<User> findAll();

	void delete(String id);

	User create(User user);

	String getTokenFromEmail(String email);
}
