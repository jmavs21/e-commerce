package com.ecommerce.service;

import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.ecommerce.model.Category;

public interface ICategoryService {

	Iterable<Category> findAll(Sort sort);

	Optional<Category> findById(String id);

	Category save(Category customer);

	void delete(String id);
}
