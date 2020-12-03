package com.ecommerce.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecommerce.model.Category;

public interface ICategoryRepo extends MongoRepository<Category, String>{

}