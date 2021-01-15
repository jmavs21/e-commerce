package com.ecom.domain.users;

import org.springframework.data.mongodb.repository.MongoRepository;

interface UserRepo extends MongoRepository<User, String> {

  User findByEmail(String email);
}
