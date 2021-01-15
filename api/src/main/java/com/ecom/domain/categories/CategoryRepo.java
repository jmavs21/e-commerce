package com.ecom.domain.categories;

import org.springframework.data.mongodb.repository.MongoRepository;

interface CategoryRepo extends MongoRepository<Category, String> {}
