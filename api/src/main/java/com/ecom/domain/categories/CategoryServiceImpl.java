package com.ecom.domain.categories;

import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
class CategoryServiceImpl implements CategoryService {

  private CategoryRepo categoryRepo;

  public CategoryServiceImpl(CategoryRepo categoryRepo) {
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
