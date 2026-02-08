package com.capri.restaurant.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.capri.restaurant.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	List<Category> findByStatus(Boolean status, Sort sort);
	
	List<Category> findByOrderByNameAsc();
	
}
