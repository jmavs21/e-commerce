package com.ecommerce.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.model.User;
import com.ecommerce.repo.IUserRepo;
import com.ecommerce.service.IUserService;
import com.ecommerce.web.error.BadRequestException;

@Service
public class UserServiceImpl implements IUserService {

	private IUserRepo userRepo;
	private PasswordEncoder passwordEncoder;
	private JwtUserDetailsService userDetailsService;

	public UserServiceImpl(IUserRepo userRepo, PasswordEncoder passwordEncoder,
			JwtUserDetailsService userDetailsService) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public Iterable<User> findAll() {
		return userRepo.findAll();
	}

	@Override
	public Optional<User> findById(String id) {
		return userRepo.findById(id);
	}

	@Override
	public User save(User user) {
		return userRepo.save(user);
	}

	@Override
	public void delete(String id) {
		userRepo.deleteById(id);
	}

	@Override
	public User create(User user) {
		User userDB = userRepo.findByEmail(user.getEmail());
		if (userDB != null)
			throw new BadRequestException("User already registered.");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = save(user);
		return user;
	}

	@Override
	public String getTokenFromEmail(String email) {
		return userDetailsService.getGeneratedToken(email);
	}
}
