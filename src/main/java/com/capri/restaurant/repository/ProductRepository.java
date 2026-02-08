package com.capri.restaurant.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.capri.restaurant.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByStatus(Boolean status, Sort sort);
	
	List<Product> findAllByOrderByCategoryNameAsc();
	
	List<Product> findAllByOrderByCategoryNameDesc();
	
	List<Product> findAll(Sort sort);

	List<Product> findByStatusTrueOrderByCategoryNameAscNameAsc();
	
}
