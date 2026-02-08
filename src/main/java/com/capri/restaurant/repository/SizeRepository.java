package com.capri.restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capri.restaurant.model.Size;

public interface SizeRepository extends JpaRepository<Size, Long> {
	
	List<Size> findByOrderByNameAsc();
	
}
