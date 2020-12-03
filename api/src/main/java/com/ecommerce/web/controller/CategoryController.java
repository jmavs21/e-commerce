package com.ecommerce.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.model.Category;
import com.ecommerce.service.ICategoryService;
import com.ecommerce.web.dto.CategoryDto;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

	private ICategoryService categoryService;
	private ModelMapper modelMapper;

	public CategoryController(ICategoryService categoryService, ModelMapper modelMapper) {
		this.categoryService = categoryService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public Collection<CategoryDto> findAll() {
		Iterable<Category> categorys = categoryService.findAll(Sort.by("name"));
		List<CategoryDto> categorysDtos = new ArrayList<>();
		categorys.forEach(c -> categorysDtos.add(entityToDto(c)));
		return categorysDtos;
	}

	@GetMapping(value = "/{id}")
	public CategoryDto findOne(@PathVariable String id) {
		Category category = categoryService.findById(id)
				.orElseThrow(() -> new DataRetrievalFailureException("The category with the given Id was not found."));
		return entityToDto(category);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryDto create(@Valid @RequestBody CategoryDto newCategory) {
		return entityToDto(categoryService.save(dtoToEntity(newCategory)));
	}

	@PutMapping("/{id}")
	public CategoryDto updateCategory(@PathVariable("id") String id, @Valid @RequestBody CategoryDto updatedCategory) {
		Category category = categoryService.findById(id)
				.orElseThrow(() -> new DataRetrievalFailureException("The category with the given Id was not found."));
		category.setName(updatedCategory.getName());
		category.setIcon(updatedCategory.getIcon());
		category.setBackgroundColor(updatedCategory.getBackgroundColor());
		category.setColor(updatedCategory.getColor());
		return entityToDto(categoryService.save(category));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable("id") String id) {
		categoryService.findById(id)
				.orElseThrow(() -> new DataRetrievalFailureException("The category with the given Id was not found."));
		categoryService.delete(id);
	}

	protected CategoryDto entityToDto(Category entity) {
		return modelMapper.map(entity, CategoryDto.class);
	}

	protected Category dtoToEntity(CategoryDto dto) {
		return modelMapper.map(dto, Category.class);
	}
}
