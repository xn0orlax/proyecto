package com.capri.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capri.restaurant.model.BusinessDay;
import com.capri.restaurant.model.enums.BusinessDayStatus;

public interface BusinessDayRepository extends JpaRepository<BusinessDay, Long> {
	
	Optional<BusinessDay> findByStatus(BusinessDayStatus status);
	
	List<BusinessDay> findAllByOrderByOpenedAtDesc();
	
	boolean existsByStatus(BusinessDayStatus status);
	
}
