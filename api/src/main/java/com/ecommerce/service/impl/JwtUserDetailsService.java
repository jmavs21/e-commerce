package com.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.config.JwtTokenUtil;
import com.ecommerce.model.User;
import com.ecommerce.repo.IUserRepo;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private IUserRepo userRepo;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(email);
		if (user == null)
			throw new DataRetrievalFailureException("No user found with username: " + email);
		return user;
	}

	public String getGeneratedToken(String email) {
		return jwtTokenUtil.generateToken((User) loadUserByUsername(email));
	}
}
