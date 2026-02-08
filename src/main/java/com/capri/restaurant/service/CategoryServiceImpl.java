package com.capri.restaurant.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.capri.restaurant.model.Category;
import com.capri.restaurant.repository.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public Optional<Category> getById(Long id) {
		return categoryRepository.findById(id);
	}

	@Override
	public void deleteById(Long id) {
		categoryRepository.deleteById(id);
	}

	@Override
	public List<Category> findByStatus(Boolean status) {
		return categoryRepository.findByStatus(status, Sort.by(Sort.Direction.ASC, "name"));
	}

	@Override
	public List<Category> findAllOrderByNameAsc() {
		return categoryRepository.findByOrderByNameAsc();
	}

}
