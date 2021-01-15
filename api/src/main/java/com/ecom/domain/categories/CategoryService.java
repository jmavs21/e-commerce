package com.ecom.domain.categories;

import java.util.Optional;

import org.springframework.data.domain.Sort;

public interface CategoryService {

  Iterable<Category> findAll(Sort sort);

  Optional<Category> findById(String id);

  Category save(Category customer);

  void delete(String id);
}
