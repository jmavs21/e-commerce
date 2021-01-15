package com.ecom.domain.users;

import java.util.Optional;

public interface UserService {

  Optional<User> findById(String id);

  User findByEmail(String email);

  User save(User user);

  Iterable<User> findAll();

  void delete(String id);

  User create(User user, String encodedPassword);
}
