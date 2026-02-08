package com.capri.restaurant.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.capri.restaurant.model.Category;
import com.capri.restaurant.model.Product;

public interface ProductService {
	
	List<Product> findAll();

	Product save(Product product);

	Optional<Product> findById(Long id);

	void delete(Long id);
	
	List<Product> findByStatus(Boolean status);

	List<Product> findAllByOrderByCategoryName();
	
	Map<Category, List<Product>> findProductsGroupedByCategoryOrderByProductNameAsc();

	List<Product> findByStatusTrueOrderByCategoryNameAscNameAsc();
}
