package com.capri.restaurant.service;

import java.util.List;
import java.util.Optional;

import com.capri.restaurant.model.Size;

public interface SizeService {
	
	List<Size> findAll();

	Size save(Size product);

	Optional<Size> getById(Long id);

	void delete(Long id);
	
	List<Size> findAllOrderByNameAsc();
	
}
