package com.capri.restaurant.service;

import java.util.List;
import java.util.Optional;

import com.capri.restaurant.model.Category;

public interface CategoryService {
	
	List<Category> findAll();
	
	List<Category> findAllOrderByNameAsc();

	Category save(Category category);

	Optional<Category> getById(Long id);

	void deleteById(Long id);
	
	List<Category> findByStatus(Boolean status);
}
