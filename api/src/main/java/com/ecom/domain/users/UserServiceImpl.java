package com.ecom.domain.users;

import java.util.Optional;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.ecom.error.BadRequestException;

@Service
class UserServiceImpl implements UserService {

  private UserRepo userRepo;

  public UserServiceImpl(UserRepo userRepo) {
    this.userRepo = userRepo;
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
  public User findByEmail(String email) {
    User user = userRepo.findByEmail(email);
    if (user == null) throw new DataRetrievalFailureException("No user found with email: " + email);
    return user;
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
  public User create(User user, String encodedPassword) {
    User userDB = userRepo.findByEmail(user.getEmail());
    if (userDB != null) throw new BadRequestException("User already registered.");
    user.setPassword(encodedPassword);
    user = save(user);
    return user;
  }
}
