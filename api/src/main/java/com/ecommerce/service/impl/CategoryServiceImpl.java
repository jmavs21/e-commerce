package com.ecommerce.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Category;
import com.ecommerce.repo.ICategoryRepo;
import com.ecommerce.service.ICategoryService;

@Service
public class CategoryServiceImpl implements ICategoryService {

	private ICategoryRepo categoryRepo;

	public CategoryServiceImpl(ICategoryRepo categoryRepo) {
		this.categoryRepo = categoryRepo;
	}

	@Override
	public Iterable<Category> findAll(Sort sort) {
		return categoryRepo.findAll(sort);
	}

	@Override
	public Optional<Category> findById(String id) {
		return categoryRepo.findById(id);
	}

	@Override
	public Category save(Category customer) {
		return categoryRepo.save(customer);
	}

	@Override
	public void delete(String id) {
		categoryRepo.deleteById(id);
	}
}
