package com.capri.restaurant.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capri.restaurant.model.Category;
import com.capri.restaurant.model.Product;
import com.capri.restaurant.repository.ProductRepository;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public Product save(Product product) {
		return productRepository.save(product);
	}

	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);
	}

	public void delete(Long id) {
		productRepository.deleteById(id);
	}

	@Override
	public List<Product> findByStatus(Boolean status) {
		return productRepository.findByStatus(status, Sort.by(Sort.Direction.ASC, "name"));
	}

	@Override
	public List<Product> findAllByOrderByCategoryName() {
		return productRepository.findAllByOrderByCategoryNameAsc();
	}

	@Override
	public Map<Category, List<Product>> findProductsGroupedByCategoryOrderByProductNameAsc() {
		Sort sort = Sort.by(Sort.Order.asc("category.name"), Sort.Order.asc("name"));
		List<Product> products = productRepository.findAll(sort);
		return products.stream().collect(Collectors.groupingBy(Product::getCategory, LinkedHashMap::new, Collectors.toList()));
	}

	@Override
	public List<Product> findByStatusTrueOrderByCategoryNameAscNameAsc() {
		return productRepository.findByStatusTrueOrderByCategoryNameAscNameAsc();
	}
}
