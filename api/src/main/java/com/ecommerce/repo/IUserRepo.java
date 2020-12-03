package com.ecommerce.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecommerce.model.User;

public interface IUserRepo extends MongoRepository<User, String> {

	User findByEmail(String email);
}
